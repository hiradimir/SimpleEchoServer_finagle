package com.yutagithub.finagle

import scala.collection.JavaConversions._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http.Http
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future

object EchoService {

  def main(args: Array[String]) {
    val echoServer: Server = ServerBuilder()
      .codec(Http())
      .bindTo(new java.net.InetSocketAddress(8081))
      .name("EchoServer")
      .build(staticResourceFilter andThen new EchoService())
  }
  
  val staticResourceFilter =
    new SimpleFilter[HttpRequest, HttpResponse] {
      def apply(
        request: HttpRequest,
        continue: Service[HttpRequest, HttpResponse]) = {
        if (request.getUri.contains("echo")) {
          continue(request)
        } else {
          val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
          
          val b = new StringBuffer
          scala.io.Source.fromFile(new java.io.File("test.html"), "UTF-8").getLines.foreach(l=>{
            b.append(l);
          })
          
          response.setContent(ChannelBuffers.copiedBuffer(b.toString, CharsetUtil.UTF_8))
          Future.value(response)
        }
      }
    }

  /**
   * A very simple server that simply echos back request parameters.
   */
  class EchoService extends Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
      // This is how you parse request parameters
      val params = new QueryStringDecoder(request.getUri()).getParameters()
      val responseContent = params.toString()
      // This is how you write to response        
      response.setContent(ChannelBuffers.copiedBuffer(request.getHeaders.toString + "\n" + responseContent, CharsetUtil.UTF_8))
      Future.value(response)
    }
  }

}

