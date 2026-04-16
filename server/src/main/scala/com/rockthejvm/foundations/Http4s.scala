package com.rockthejvm.foundations

import cats._
import cats.implicits._
import io.circe.generic.auto.*
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.headers.*
import org.http4s.*
import cats.effect.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import org.typelevel.ci.CIString
import org.http4s.ember.server.EmberServerBuilder

import java.util.UUID

object Http4s extends IOApp.Simple {

  // simulate an HTTP server with "students" and "courses"
  type Student = String
  case class Instructor(firstName: String, lastName: String)
  case class Course(id: String, title: String, year: Int, students: List[Student], instructorName: String)
  
  object CourseRepository {
    // a "database"
    val catsEffectCourse = Course(
      "7097863a-4797-4bf2-9fcf-c09911859c45",
      "Rock the JVM Ultimate Scala course",
      2022,
      List("Padhu", "Adi"),
      "Martin Odersky"
    )
    val courses: Map[String, Course] = Map(catsEffectCourse.id -> catsEffectCourse)  
  
    // API
    def findCourseById(courseId: UUID): Option[Course] =
      courses.get(courseId.toString)
  
    def findCoursesByInstructor(name: String): List[Course] =
      courses.values.filter(_.instructorName == name).toList
  }
  
  // essential REST endpoints
  // GET localhost:8080/courses?instructor=Martin%20Odersky&year=2022
  // GET localhost:8080/courses/7097863a-4797-4bf2-9fcf-c09911859c45/students
  
  object InstructorQueryParamMatcher extends QueryParamDecoderMatcher[String]("instructor")
  object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Int]("year")
  
  def courseRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*
    
    HttpRoutes.of[F] {
      case GET -> Root / "courses" :? InstructorQueryParamMatcher(instructor) +& YearQueryParamMatcher(maybeYear) =>
        val courses = CourseRepository.findCoursesByInstructor(instructor)
        //println(courses.asJson)
        maybeYear match {
          case Some(y) => y.fold(
            _ => BadRequest("Parameter 'year' is invalid"),
            year => Ok(courses.filter(_.year == year).asJson)
          )
          case None => Ok(courses.asJson)
        }
      case GET -> Root / "courses" / UUIDVar(courseId) / "students" =>
        CourseRepository.findCourseById(courseId).map(_.students) match {
          case Some(students) => Ok(students.asJson, Header.Raw(CIString("My-custom-header"), "rockthejvm"))
          case None => NotFound(s"No course with $courseId was found")
        }
    }
  }

  def healthEndPoint[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "health" => Ok("All going great!")
    }
  }

  def allROutes[F[_]: Monad]: HttpRoutes[F] = courseRoutes[F] <+> healthEndPoint[F]

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
