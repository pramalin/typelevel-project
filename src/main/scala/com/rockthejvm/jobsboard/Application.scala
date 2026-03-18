package com.rockthejvm.jobsboard

import cats.*
import cats.implicits.*
import cats.effect.{IO, IOApp}
import org.http4s.ember.server.EmberServerBuilder

import com.rockthejvm.jobsboard.http.routes.HealthRoutes
import com.rockthejvm.jobsboard.config.*
import com.rockthejvm.jobsboard.config.syntax.*
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException

object Application extends IOApp.Simple {

  val configSource = ConfigSource.default.load[EmberConfig]

  override def run = ConfigSource.default.loadF[IO, EmberConfig].flatMap { config =>
    EmberServerBuilder
      .default[IO]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(HealthRoutes[IO].routes.orNotFound)
      .build
      .use(_ => IO.println("Job boards ready") *> IO.never)
    }
}