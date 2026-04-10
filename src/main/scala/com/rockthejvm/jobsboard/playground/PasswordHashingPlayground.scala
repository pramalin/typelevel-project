package com.rockthejvm.jobsboard.playground

import cats.effect.IOApp
import cats.effect.IO
import tsec.passwordhashers.jca.BCrypt
import tsec.passwordhashers.PasswordHash


object PasswordHashingPlayground extends IOApp.Simple {
    override def run: IO[Unit] = 
        BCrypt.hashpw[IO]("scalarocks").flatMap(IO.println) *>
        BCrypt
            .checkpwBool[IO](
                "scalarocks",
                PasswordHash[BCrypt]("$2a$10$4ghT6c5sC6..iXIE2gcDgeYXrugZ/km5zqSWPo1qe0Zf6xmyOJV0S")
            )
            .flatMap(IO.println)
}
