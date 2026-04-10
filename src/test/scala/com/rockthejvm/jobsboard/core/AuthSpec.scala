package com.rockthejvm.jobsboard.core

import cats.effect.*
import cats.data.OptionT
import org.scalatest.freespec.AsyncFreeSpec
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import tsec.mac.jca.HMACSHA256
import tsec.authentication.IdentityStore
import tsec.authentication.JWTAuthenticator

import scala.concurrent.duration.*
import com.rockthejvm.jobsboard.fixtures.* 
import com.rockthejvm.jobsboard.domain.user.*
import com.rockthejvm.jobsboard.domain.auth.*
import com.rockthejvm.jobsboard.domain.security.*
import tsec.passwordhashers.jca.BCrypt
import tsec.passwordhashers.PasswordHash

class AuthSpec 
    extends AsyncFreeSpec
    with AsyncIOSpec
    with Matchers
    with UsersFixture {
  
    given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

    private val mockedUsers: Users[IO] = new Users[IO] {
        override def find(email: String): IO[Option[User]] = 
            if (email == mailerEmail) IO.pure(Some(Mailer))
            else IO.pure(None)
        override def create(user: User): IO[String] = IO.pure(user.email)
        override def update(user: User): IO[Option[User]] = IO.pure(Some(user))
        override def delete(email: String): IO[Boolean] = IO.pure(true)
    }

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

    "Auth 'algebra'" - {
        "login should return None if the user doesn't exists" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                maybeToken <- auth.login("user@rockthejvm", "password")
            } yield maybeToken
            
            program.asserting(_ shouldBe None)
        }


        "login should return None if the user exists but the password is wrong" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                maybeToken <- auth.login(mailerEmail, "wrongpassword")
            } yield maybeToken
            
            program.asserting(_ shouldBe None)
        }
    
        "login should return token if the user exists and the password is correct" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                maybeToken <- auth.login(mailerEmail, "rockthejvm")
            } yield maybeToken
            
            program.asserting(_ shouldBe defined)
        }
    
        "signing up should not create a user with an existing email" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                maybeUser <- auth.signUp(NewUserInfo(
                 mailerEmail,
                 "somepassword",
                 Some("John"),
                 Some("Doe"),
                 Some("Some Company")
                ))
            } yield maybeUser
            
            program.asserting(_ shouldBe None)
        }
    
        "signing up should create a completely new user" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                maybeUser <- auth.signUp(NewUserInfo(
                 "bob@rockthejvm.com",
                 "somePassword",
                 Some("Bob"),
                 Some("Jones"),
                 Some("Company")
                ))
            } yield maybeUser
            
            program.asserting{
                case Some(user) =>
                    user.email shouldBe "bob@rockthejvm.com"
                    user.firstName shouldBe Some("Bob")
                    user.lastName shouldBe Some("Jones")
                    user.company shouldBe Some("Company")
                    user.role should not be Role.RECRUITER

                case None => fail()
            }
        }
    
        "changePassword should return Right(None) if the user doesn't esist" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                result <- auth.changePassword("alice@rockthejavm.com", NewPasswordInfo("oldpw", "newpw"))
            } yield result 
            
            program.asserting(_ shouldBe Right(None))
        }
    
        "changePassword should return Left with an error if the user esists and the password is incorrect" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                result <- auth.changePassword(mailerEmail, NewPasswordInfo("oldpw", "newpw"))
            } yield result 
            
            program.asserting(_ shouldBe Left("Invalid password"))
        }
    
        "changePassword should correctly change password if all details are correct" in {
            val program = for {
                auth <- LiveAuth[IO](mockedUsers, mockedAuthenticator)
                result <- auth.changePassword(mailerEmail, NewPasswordInfo("rockthejvm", "scalarocks"))
                isNicePasswprd <- result match {
                    case Right(Some(user)) =>
                        BCrypt
                            .checkpwBool[IO](
                                "scalarocks",
                                PasswordHash[BCrypt](user.hashedPassword)
                            )
                    case _ =>
                        IO.pure(false)
                }
            } yield isNicePasswprd
            
            program.asserting(_ shouldBe true)
        }
    
    }
}
