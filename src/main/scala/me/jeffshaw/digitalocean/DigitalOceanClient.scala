package me.jeffshaw.digitalocean

import java.util.concurrent.TimeoutException
import me.jeffshaw.digitalocean.ToFuture._
import org.asynchttpclient.{AsyncHttpClient, Request, RequestBuilder, Response}
import org.json4s._
import org.json4s.native._
import scala.concurrent.{ExecutionContext, Future}
import scala.collection.JavaConverters._
import scala.concurrent.duration._

/**
 *
 * @param token Your API token.
 * @param maxWaitPerRequest The maximum amount of time the client should wait for a response before assuming the service is down.
 * @param actionCheckInterval The amount of time to wait between checks for an action to complete.
 */
case class DigitalOceanClient(
  private val token: String,
  maxWaitPerRequest: Duration,
  actionCheckInterval: Duration
)(implicit client: AsyncHttpClient
) extends DelayedFuture {
  private val requestPrefix: Request =
    new RequestBuilder(DigitalOceanClient.host).
      addHeader("Authorization", "Bearer " + token).
      build()

  /**
   * This needs to be used carefully, because it can potentially give
   * the api key to a 3rd party.
   *
   * @param req
   * @tparam T
   * @return
   */
  private[digitalocean] def customRequest[T: Manifest](
    req: Request
  )(implicit ec: ExecutionContext
  ): Future[T] = {
    val request =
      new RequestBuilder(req).addHeader("Authorization", "Bearer " + token)
    parseResponse[T](client.executeRequest(request))
  }

  def createRequest(
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  ): RequestBuilder = {
    val javaQueryParameters = {
      for ((key, value) <- queryParameters) yield
        key -> value.asJava
    }.asJava
    val url = requestPrefix.getUrl + path.mkString("/", "/", "")
    new RequestBuilder(requestPrefix).setUrl(url).setQueryParams(javaQueryParameters)
  }

  def delete(
    path: Seq[String],
    maybeMessage: Option[JValue] = None,
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit ec: ExecutionContext
  ): Future[Unit] = {
    val request = createRequest(path, queryParameters).setMethod("DELETE")

    for {
      message <- maybeMessage
    } {
      val messageBody = JsonMethods.compact(JsonMethods.render(message.snakizeKeys))
      request.setBody(messageBody)
    }

    for {
      response <- client.executeRequest(request)
    } yield {
      if (response.getStatusCode >= 300) {
        throw DigitalOceanClientException(response)
      }
    }
  }

  private def parseResponse[T: Manifest](
    request: Future[Response]
  )(implicit ec: ExecutionContext
  ): Future[T] = {
    for {
      response <- request
    } yield {
      val responseBody = response.getResponseBody

      val statusCode = response.getStatusCode

      if (statusCode < 300 &&
        response.getContentType.startsWith(DigitalOceanClient.contentType)
      ) {
        try parseJson(responseBody).camelizeKeys.extract[T]
        catch {
          case e: MappingException =>
            throw DigitalOceanClientException(response, cause = Some(e))
        }
      } else {
        throw DigitalOceanClientException(response)
      }
    }
  }

  def get[T: Manifest](
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit ec: ExecutionContext
  ): Future[T] = {
    val request = client.executeRequest(createRequest(path, queryParameters).setMethod("GET"))
    parseResponse[T](request)
  }

  def exists(
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit ec: ExecutionContext
  ): Future[Boolean] = {
    val request = client.executeRequest(createRequest(path, queryParameters).setMethod("HEAD"))
    for {
      response <- request
    } yield {
      response.getStatusCode != 404
    }
  }

  def post[T: Manifest](
    path: Seq[String],
    message: JValue,
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit ec: ExecutionContext
  ): Future[T] = {
    val messageBody = JsonMethods.compact(JsonMethods.render(message.snakizeKeys))
    val request = client.executeRequest(createRequest(path = path).setBody(messageBody).setMethod("POST"))
    parseResponse[T](request)
  }

  def postWithEmptyResponse(
    path: Seq[String],
    message: JValue,
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit ec: ExecutionContext
  ): Future[Unit] = {
    val messageBody = JsonMethods.compact(JsonMethods.render(message.snakizeKeys))
    val request = client.executeRequest(createRequest(path = path).setBody(messageBody).setMethod("POST"))

    for {
      response <- request
    } yield {
      if (response.getStatusCode != 204) {
        throw DigitalOceanClientException(response)
      }
    }
  }

  def put[T: Manifest](
    path: Seq[String],
    message: JValue,
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit ec: ExecutionContext
  ): Future[T] = {
    val messageBody = JsonMethods.compact(JsonMethods.render(message.snakizeKeys))
    val request = client.executeRequest(createRequest(path = path).setBody(messageBody).setMethod("PUT"))
    parseResponse[T](request)
  }

  /**
    * Repeatedly execute a function until a predicate is satisfied, with a delay of
    * [[actionCheckInterval]] between executions, and a maximum of [[maxWaitPerRequest]]
    * to wait per execution.
    *
    * @param pollAction
    * @param predicate
    * @tparam T
    * @return
    */
  private[digitalocean] def poll[T](
    pollAction: => Future[T],
    predicate: T => Boolean
  )(implicit ec: ExecutionContext
  ): Future[T] = {
    val whenTimeout = after(maxWaitPerRequest)(Future.failed(new TimeoutException()))
    val firstCompleted = Future.firstCompletedOf(Seq(pollAction, whenTimeout))
    for {
      result <- firstCompleted
      completeResult <-
        if (predicate(result)) Future.successful(result)
        else sleep(actionCheckInterval).flatMap(_ => poll(pollAction, predicate))
    } yield completeResult
  }
}

object DigitalOceanClient {
  val contentType = "application/json"

  val host =
    new RequestBuilder().
      setUrl("https://api.digitalocean.com/v2").
      addHeader("Content-Type", contentType).
      build()
}
