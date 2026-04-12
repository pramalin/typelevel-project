package com.rockthejvm.jobsboard.http.routes

import cats.effect.*
import cats.implicits.*
import cats.data.OptionT
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.implicits.*
import org.scalatest.freespec.AsyncFreeSpec
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import tsec.mac.jca.HMACSHA256
import tsec.authentication.IdentityStore
import tsec.authentication.JWTAuthenticator

import scala.concurrent.duration.*
import com.rockthejvm.jobsboard.domain.user.*
import com.rockthejvm.jobsboard.domain.auth.*
import com.rockthejvm.jobsboard.domain.security.*
import com.rockthejvm.jobsboard.core.*
import com.rockthejvm.jobsboard.fixtures.*
import org.typelevel.ci.CIStringSyntax
import tsec.jws.mac.JWTMac
import org.http4s.headers.Authorization

class AuthRouteSpec
    extends AsyncFreeSpec
    with AsyncIOSpec 
    with Matchers
    with Http4sDsl[IO]
    with UserFixture {
  
    ////////////////////////////////////////////////////////////
    // prep
    ////////////////////////////////////////////////////////////

    val mockedAuthenticator: Authenticator[IO] = {
        // key for hashing
        val key = HMACSHA256.unsafeGenerateKey
        // identity store to rerieve users
        val idStore: IdentityStore[IO, String, User]  = (email: String) =>
            if (email == mailerEmail) OptionT.pure(Mailer)
            else if (email == riccardoEmail) OptionT.pure(Riccardo)
            else OptionT.none[IO, User]
        
        // jwt authenticator
        JWTAuthenticator.unbacked.inBearerToken(
            1.day, // expiration of tokens
            None,  // max idle time (optional)
            idStore, //  identity store
            key // hash key
        )
    }

    val mockedAuth: Auth[IO] = new Auth[IO] {
        // TODO make sure only the mailer user exists in the DB before running the tests
        def login(email: String, password: String): IO[Option[JwtToken]] =
          if(email == mailerEmail && password == mailerPassword) {
            mockedAuthenticator.create(mailerEmail).map(Some(_))
          } else IO.pure(None)

        def signUp(newUserInfo: NewUserInfo): IO[Option[User]] =
            if(newUserInfo.email == riccardoEmail)
             Some(Riccardo).pure[IO]
            else IO.pure(None)

        def changePassword(
            email: String,
            newPasswordInfo: NewPasswordInfo
        ): IO[Either[String, Option[User]]] =
            if(email == mailerEmail)
               if(newPasswordInfo.oldPassword == mailerPassword)
                 IO.pure(Right(Some(Mailer)))
               else
                 IO.pure(Left("Invalid password"))
            else
               IO.pure(Right(None)) 

        def authenticator: Authenticator[IO] = mockedAuthenticator
    }

    extension (r: Request[IO])
        def withBearerToken(a: JwtToken): Request[IO] =
            r.putHeaders {
                val jwtString = JWTMac.toEncodedString[IO, HMACSHA256](a.jwt)
                // Authorization: Beared {jwt}
                Authorization(Credentials.Token(AuthScheme.Bearer, jwtString))
            }
    given logger: Logger[IO] = Slf4jLogger.getLogger[IO]
    val authRoutes: HttpRoutes[IO] = AuthRoutes[IO](mockedAuth).routes


    ////////////////////////////////////////////////////////////
    // tests
    ////////////////////////////////////////////////////////////

    "AuthRoutes" - {

        "should return a 401 - unauthorized if login fails" in {
            for {
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.POST, uri = uri"/auth/login")
                    .withEntity(LoginInfo(mailerEmail, "wrongpassword"))
                )
            } yield {
                response.status shouldBe Status.Unauthorized
            }
        }

        "should return a 200 - OK + a jwt if login is successful" in {
            for {
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.POST, uri = uri"/auth/login")
                    .withEntity(LoginInfo(mailerEmail, mailerPassword))
                )
            } yield {
                response.status shouldBe Status.Ok
                response.headers.get(ci"Authorization") shouldBe defined
            }
        }

        "should return a 400 - Bad Request if the user to create already exists" in {
            for {
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.POST, uri = uri"/auth/users")
                    .withEntity(NewUserMailer)
                )
            } yield {
                response.status shouldBe Status.BadRequest
            }
        }

        "should return a 201 - Created est if the user creation succeeds" in {
            for {
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.POST, uri = uri"/auth/users")
                    .withEntity(NewUserRiccardo)
                )
            } yield {
                response.status shouldBe Status.Created
            }
        }

        "should return a 200 - OK if logging out with a valid JWT token" in {
            for {
                jwtToken <- mockedAuthenticator.create(mailerEmail)
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.POST, uri = uri"/auth/logout")
                    .withBearerToken(jwtToken)
                )
            } yield {
                response.status shouldBe Status.Ok
            }
        }

        // change password - user doesn't exist => 404 Not Found
        "should return a 404 - Not found if changing password for a user that doesn't exist" in {
            for {
                jwtToken <- mockedAuthenticator.create(riccardoEmail)
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.PUT, uri = uri"/auth/users/password")
                    .withBearerToken(jwtToken)
                    .withEntity(NewPasswordInfo(riccardoPassword, "newpassword"))
                )
            } yield {
                response.status shouldBe Status.NotFound
            }
        }

        // change password - invalid old password => 403 Forbidden
        "should return a 403 - Forbidden if old password is incorrect" in {
            for {
                jwtToken <- mockedAuthenticator.create(mailerEmail)
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.PUT, uri = uri"/auth/users/password")
                    .withBearerToken(jwtToken)
                    .withEntity(NewPasswordInfo("wrongpassword", "newpassword"))
                )
            } yield {
                response.status shouldBe Status.Forbidden
            }
        }

        // change password - user JWT is invalid => 401 Unauthorized
        "should return a 401 - Unauthorized if changing password without a JWT" in {
            for {
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.PUT, uri = uri"/auth/users/password")
                    .withEntity(NewPasswordInfo(mailerPassword, "newpassword"))
                )
            } yield {
                response.status shouldBe Status.Unauthorized
            }
        }

        // change password - successful => 200 OK
        "should return a 200 - OK if changing password for a user with valid JWT and password" in {
            for {
                jwtToken <- mockedAuthenticator.create(mailerEmail)
                response <- authRoutes.orNotFound.run(
                    Request(method = Method.PUT, uri = uri"/auth/users/password")
                    .withBearerToken(jwtToken)
                    .withEntity(NewPasswordInfo(mailerPassword, "newpassword"))
                )
            } yield {
                response.status shouldBe Status.Ok
            }
        }

    }
}
