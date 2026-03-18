error id: file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/http/HttpApi.scala:
file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/http/HttpApi.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb

found definition using fallback; symbol routes
offset: 326
uri: file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/http/HttpApi.scala
text:
```scala
package com.rockthejvm.jobsboard.http

import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.*
import cats.implicits.*

import com.rockthejvm.jobsboard.http.routes.*

class HttpApi[F[_]: Monad] private {
  private val healthRoutes: HttpRoutes[F] = HttpRoutes[F].routes@@
  private val healthRoutes: HttpRoutes[F] = HttpRoutes[F].routes
}

object HttpApi {
    def apply[F[_]: Monad] = new HttpApi[F]
}   

```


#### Short summary: 

empty definition using pc, found symbol in pc: 