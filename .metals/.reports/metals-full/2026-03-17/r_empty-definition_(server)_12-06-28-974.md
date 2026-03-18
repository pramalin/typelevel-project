error id: file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/Main.scala:
file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/Main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -cats/effect/HttpRoutes#
	 -org/http4s/dsl/HttpRoutes#
	 -org/http4s/dsl/impl/HttpRoutes#
	 -org/http4s/server/HttpRoutes#
	 -cats/HttpRoutes#
	 -HttpRoutes#
	 -scala/Predef.HttpRoutes#
offset: 400
uri: file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/Main.scala
text:
```scala
package com.rockthejvm.jobsboard

import cats.effect.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.*
import cats.effect.{IO, IOApp}

object Application extends IOApp {

    /* 
       1 - add a plain health endpoint to our app
       2 - add minimal configuration
       3 - basic http server layout 
     */


  def healthEndPoint[F[_]: Monad]: HttpRo@@utes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "health" => Ok("All going great!")
    }
  }

  def allROutes[F[_]: Monad]: HttpRoutes[F] = healthEndPoint[F]

  def routerWithPathPrefixes = Router(
    "/api" -> courseRoutes[IO],
    "/private" -> healthEndPoint[IO]
  ).orNotFound

  override def run: effect.IO[Unit] = EmberServerBuilder
    .default[IO]
    .withHttpApp(routerWithPathPrefixes)
    .build
    .use(_ => IO.println("Server ready") *> IO.never)

}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 