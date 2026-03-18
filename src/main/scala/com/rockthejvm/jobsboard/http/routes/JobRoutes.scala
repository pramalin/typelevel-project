package com.rockthejvm.jobsboard.http.routes

import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.*
import cats.implicits.*

class JobRoutes[F[_]: Monad] private extends Http4sDsl[F]{

  // POST /jobs/offset=x&limit=y {filters} // TODO add query params and filters
  private val allJobROute: HttpRoutes[F] =  HttpRoutes.of[F] {
    case POST -> Root =>
      Ok("TODO")
  }

  // GET /jobs/uuid
  private val findJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / UUIDVar(id)=>
      Ok(s"TODO find job with id $id")
  }

  //POST /jobs/create{ jobInfo }
  private val createJobRoute: HttpRoutes[F] =  HttpRoutes.of[F] {
    case POST -> Root / "create" =>
      Ok("TODO")
  }

  // PUT /jobs/uuid { jobInfo }
  private val updateJobRoute: HttpRoutes[F] =  HttpRoutes.of[F] {
    case PUT -> Root / UUIDVar(id) =>
      Ok(s"TODO update at id $id")
  }


  // DELETE /jobs/uuid
  private val deleteJobRoute: HttpRoutes[F] =  HttpRoutes.of[F] {
    case DELETE -> Root / UUIDVar(id) =>
      Ok(s"TODO delete at id $id")
  }

  val routes = Router(
    "/jobs" -> (allJobROute <+> findJobRoute <+> createJobRoute <+> updateJobRoute <+> deleteJobRoute)
  )
}

object JobRoutes {
    def apply[F[_]: Monad] = new JobRoutes[F]
}
