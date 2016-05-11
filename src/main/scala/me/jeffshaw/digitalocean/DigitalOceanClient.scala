package me.jeffshaw.digitalocean

import com.ning.http.client.Response
import dispatch.Defaults._
import dispatch._
import java.util.concurrent.TimeoutException
import org.json4s._
import native._
import scala.concurrent.Future
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
) extends DelayedFuture {
  private val requestPrefix: Req =
    DigitalOceanClient.host.addHeader("Authorization", "Bearer " + token)

  /**
   * This needs to be used carefully, because it can potentially give
   * the api key to a 3rd party.
   *
   * @param req
   * @tparam T
   * @return
   */
  private[digitalocean] def customRequest[T: Manifest](req: Req): Future[T] = {
    parseResponse[T](Http(req.addHeader("Authorization", "Bearer " + token)))
  }

  def createRequest(
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  ): Req = {
    path.foldLeft(requestPrefix)((accum, pathElement) => accum / pathElement).
      setQueryParameters(queryParameters)
  }

  def delete(
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  ): Future[Unit] = {
    val request = Http(createRequest(path, queryParameters) DELETE)

    for {
      response <- request
    } yield {
      if(response.getStatusCode >= 300) {
        throw new DigitalOceanClientException(response)
      }
    }
  }

  def parseResponse[T: Manifest](request: Future[Response]): Future[T] = {
    for {
      response <- request
    } yield {
      val responseBody = response.getResponseBody

      val statusCode = response.getStatusCode

      if (statusCode < 300 &&
        response.getContentType == DigitalOceanClient.contentType
      ) {
        parseJson(responseBody).camelizeKeys.extract[T]
      } else {
        throw DigitalOceanClientException(response)
      }
    }
  }

  def get[T: Manifest](
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  ): Future[T] = {
    val request = Http(createRequest(path, queryParameters) GET)
    parseResponse[T](request)
  }

  def exists(
    path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  ): Future[Boolean] = {
    val request = Http(createRequest(path, queryParameters) HEAD)
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
  ): Future[T] = {
    val messageBody = JsonMethods.compact(JsonMethods.render(message.snakizeKeys))
    val request = Http(createRequest(path = path).setBody(messageBody).POST)
    parseResponse[T](request)
  }

  def put[T: Manifest](
    path: Seq[String],
    message: JValue,
    queryParameters: Map[String, Seq[String]] = Map.empty
  ): Future[T] = {
    val messageBody = JsonMethods.compact(JsonMethods.render(message.snakizeKeys))
    val request = Http(createRequest(path = path).setBody(messageBody).PUT)
    parseResponse[T](request)
  }

  /**
    * Repeatedly execute a function until a predicate is satisfied, with a delay of
    * [[actionCheckInterval]] between executions.
 *
    * @param pollAction
    * @param predicate
    * @tparam T
    * @return
    */
  private[digitalocean] def poll[T](pollAction: => Future[T], predicate: T => Boolean): Future[T] = {
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
  val host = dispatch.host("api.digitalocean.com").secure.setContentType("application/json", "utf-8") / "v2"

  val contentType = "application/json; charset=utf-8"
}
