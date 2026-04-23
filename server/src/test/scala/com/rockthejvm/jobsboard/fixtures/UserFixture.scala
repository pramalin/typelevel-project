package com.rockthejvm.jobsboard.fixtures

import cats.effect.IO
import com.rockthejvm.jobsboard.core.Users
import com.rockthejvm.jobsboard.domain.user.*
import com.rockthejvm.jobsboard.domain.auth.*

/* 
rockthejvm => $2a$10$7XxTL6cI5UDRXhjNnrLuY.3fZQggtaa6wv2K/TyG2fE6dINoKjMNa
riccardorulez => $2a$10$cFOIo3E1e8Le7OfJ5WwLNOr/XKtMjUyuI.dAGtJMA2AEA9Q1ws6zu
simplepassword => $2a$10$1BFzCpqKxnxS2Pw3M.M5Xu8PLlVf3sHtyypZxdAWJR9lq1rrPln6S
riccardorocks => $2a$10$lAw.fj6w0ua0h32FIVc4POCVx3XgRg7GDYDfk/.mi.pAsXF84wr8e
 */
trait UserFixture {

    val mockedUsers: Users[IO] = new Users[IO] {
        override def find(email: String): IO[Option[User]] = 
            if (email == mailerEmail) IO.pure(Some(Mailer))
            else IO.pure(None)
        override def create(user: User): IO[String] = IO.pure(user.email)
        override def update(user: User): IO[Option[User]] = IO.pure(Some(user))
        override def delete(email: String): IO[Boolean] = IO.pure(true)
    }

    val Mailer = User(
        "mailer@mailinator.com",
        "$2a$10$7XxTL6cI5UDRXhjNnrLuY.3fZQggtaa6wv2K/TyG2fE6dINoKjMNa",
        Some("Mailer"),
        Some("Man"),
        Some("Rock the JVM"),
        Role.ADMIN
    )

    val mailerEmail = Mailer.email
    val mailerPassword = "rockthejvm"

    val Riccardo = User(
        "riccardo@rockthejvm.com",
        "$2a$10$cFOIo3E1e8Le7OfJ5WwLNOr/XKtMjUyuI.dAGtJMA2AEA9Q1ws6zu",
        Some("Riccardo"),
        Some("Cordin"),
        Some("Rock the JVM"),
        Role.RECRUITER
    )

    val riccardoEmail = Riccardo.email
    val riccardoPassword = "riccardorulez"

    val NewUser = User(
        "newuser@gmail.com",
        "$2a$10$1BFzCpqKxnxS2Pw3M.M5Xu8PLlVf3sHtyypZxdAWJR9lq1rrPln6S",
        Some("John"),
        Some("Doe"),
        Some("Some Company"),
        Role.ADMIN
    )

    val UpdatedRiccardo = User(
        "riccardo@rockthejvm.com",
        "$2a$10$lAw.fj6w0ua0h32FIVc4POCVx3XgRg7GDYDfk/.mi.pAsXF84wr8e",
        Some("AdminRiccardo"),
        Some("Cordin"),
        Some("Rock the Updated JVM"),
        Role.ADMIN
    )

    val NewUserMailer = NewUserInfo(
        mailerEmail,
        mailerPassword,
        Some("Mailer"),
        Some("Man"),
        Some("Rock the JVM")
    ) 

    val NewUserRiccardo = NewUserInfo(
        riccardoEmail,
        riccardoPassword,
        Some("Riccardo"),
        Some("Cordin"),
        Some("Rock the JVM")
    )
}
