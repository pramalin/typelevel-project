package com.rockthejvm.jobsboard.domain

import doobie.util.meta.Meta

object user {
    final case class User(
        email: String,
        hashedPassword: String,
        firstName: Option[String],
        lastName: Option[String],
        company: Option[String],
        role: Role
    )

    final case class NewUserInfo(
        email: String,
        hashedPassword: String,
        firstName: Option[String],
        lastName: Option[String],
        company: Option[String]
    )

     enum Role {
        case ADMIN, RECRUITER
     }

     object Role {
        given metaTole: Meta[Role] =
            Meta[String].timap[Role](Role.valueOf)(_.toString)
     }

}
