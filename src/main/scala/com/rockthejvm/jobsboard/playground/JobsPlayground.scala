package com.rockthejvm.jobsboard.playground

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.util.*
import doobie.hikari.HikariTransactor
import com.rockthejvm.jobsboard.domain.job.*
import com.rockthejvm.jobsboard.core.*

import scala.io.StdIn
object JobsPlayground extends IOApp.Simple {

  var postgresResource: Resource[IO, HikariTransactor[IO]] = for {
    ec <- ExecutionContexts.fixedThreadPool[IO](8)
    xa <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql:board",
      "docker",
      "docker",
      ec
    )
  } yield xa

  val jobInfo = JobInfo.minimal(
    company = "Rock the JVM",
    title = "Scala Developer",
    description = "We are looking for a Scala developer to join our team.",
    externalUrl = "https://rockthejvm.com/jobs/scala-developer",
    remote = true,
    location = "Remote"
  )

  override def run: IO[Unit] = postgresResource.use { xa =>
    for {
      jobs      <- LiveJobs[IO](xa)
      _         <- IO(println("Ready. Next...")) *> IO(StdIn.readLine)
      id        <- jobs.create("mailer@mailinator.com", jobInfo)
      _         <- IO(println("Next...")) *> IO(StdIn.readLine)
      list      <- jobs.all()
      _         <- IO(println(s"All jobs: $list. Next...")) *> IO(StdIn.readLine)
      _         <- jobs.update(id, jobInfo.copy(title = "x10 Scala Developer"))
      newJob    <- jobs.find(id)
      _         <- IO(println(s"New job:. $newJob. Next...")) *> IO(StdIn.readLine)
      _         <- jobs.delete(id)
      listAfter <- jobs.all()
      _         <- IO(println(s"Deleted job. List now $listAfter. Next...")) *> IO(StdIn.readLine)
    } yield ()
  }
}
