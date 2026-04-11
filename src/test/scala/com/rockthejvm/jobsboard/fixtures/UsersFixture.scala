package com.rockthejvm.jobsboard.fixtures

import com.rockthejvm.jobsboard.domain.user.User
import com.rockthejvm.jobsboard.domain.user.Role

/* 
rockthejvm => $2a$10$7XxTL6cI5UDRXhjNnrLuY.3fZQggtaa6wv2K/TyG2fE6dINoKjMNa
riccardorulez => $2a$10$cFOIo3E1e8Le7OfJ5WwLNOr/XKtMjUyuI.dAGtJMA2AEA9Q1ws6zu
simplepassword => $2a$10$1BFzCpqKxnxS2Pw3M.M5Xu8PLlVf3sHtyypZxdAWJR9lq1rrPln6S
riccardorocks => $2a$10$lAw.fj6w0ua0h32FIVc4POCVx3XgRg7GDYDfk/.mi.pAsXF84wr8e
 */
trait UsersFixture {
    val Mailer = User(
        "mailer@mailinator.com",
        "$2a$10$7XxTL6cI5UDRXhjNnrLuY.3fZQggtaa6wv2K/TyG2fE6dINoKjMNa",
        Some("Mailer"),
        Some("Man"),
        Some("Rock the JVM"),
        Role.ADMIN
    )

    val mailerEmail = Mailer.email

    val Riccardo = User(
        "riccardo@rockthejvm.com",
        "$2a$10$cFOIo3E1e8Le7OfJ5WwLNOr/XKtMjUyuI.dAGtJMA2AEA9Q1ws6zu",
        Some("Riccardo"),
        Some("Cordin"),
        Some("Rock the JVM"),
        Role.RECRUITER
    )

    val riccardoEmail = Riccardo.email

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

}
