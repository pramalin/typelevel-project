package com.rockthejvm.jobsboard.fixtures

import com.rockthejvm.jobsboard.domain.user.User
import com.rockthejvm.jobsboard.domain.user.Role

trait UsersFixture {
    val Mailer = User(
        "mailer@mailinator.com",
        "rockthejvm",
        Some("Mailer"),
        Some("Man"),
        Some("Rock the JVM"),
        Role.ADMIN
    )

    val Riccardo = User(
        "riccardo@rockthejvm.com",
        "riccardorulez",
        Some("Riccardo"),
        Some("Cordin"),
        Some("Rock the JVM"),
        Role.RECRUITER
    )

    val NewUser = User(
        "newuser@gmail.com",
        "simplepassword",
        Some("John"),
        Some("Doe"),
        Some("Some Company"),
        Role.ADMIN
    )

    val UpdatedRiccardo = User(
        "riccardo@rockthejvm.com",
        "riccardorocks",
        Some("AdminRiccardo"),
        Some("Cordin"),
        Some("Rock the Updated JVM"),
        Role.ADMIN
    )

}
