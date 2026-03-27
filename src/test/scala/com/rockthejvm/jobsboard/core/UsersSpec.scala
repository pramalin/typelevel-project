package com.rockthejvm.jobsboard.core

import cats.effect.*
import doobie.implicits.*
import org.scalatest.freespec.AsyncFreeSpec
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import com.rockthejvm.jobsboard.fixtures.*

import com.rockthejvm.jobsboard.domain.user.* 
import org.scalatest.Inside
import org.postgresql.util.PSQLException

class UsersSpec
    extends AsyncFreeSpec
    with AsyncIOSpec
    with Matchers
    with Inside
    with DoobieSpec
    with UsersFixture {
    override val initScript: String = "sql/users.sql"
    given logger: Logger[IO] = Slf4jLogger.getLogger[IO]


    "Users 'algebra'" - {
        "Should retrieve a user by email" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    retrieved <- users.find("riccardo@rockthejvm.com")
                } yield retrieved

                program.asserting(_ shouldBe Some(Riccardo))
            }
        }

        "Should return None if the email doesn't exist" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    retrieved <- users.find("notfound@rockthejvm.com")
                } yield retrieved

                program.asserting(_ shouldBe None)
            }
        }

        "Should create a new user" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    userid <- users.create(NewUser)
                    maybeUser <- sql"select * from users where email = ${NewUser.email}"
                                .query[User]
                                .option
                                .transact(xa)
                } yield (userid, maybeUser)

                program.asserting {
                  case (userid, maybeUser) =>
                    userid shouldBe NewUser.email
                    maybeUser shouldBe Some(NewUser)
                }
            }
        }

        "Should fail creating a new user if the email already exists" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    userid <- users.create(Mailer).attempt // IO[Either[Throwable, String]]
                } yield userid

                program.asserting { outcome =>
                    inside(outcome) {
                        case Left(e) => e shouldBe a[PSQLException]
                        case _ => fail()
                    }
                    
                }
            }
        }

        "Should return None when updating a user that does not exist" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    maybeUser <- users.update(NewUser)
                } yield maybeUser

                program.asserting(_ shouldBe None)
            }
        }

        "Should update an existing user" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    maybeUser <- users.update(UpdatedRiccardo)
                } yield maybeUser

                program.asserting(_ shouldBe Some(UpdatedRiccardo))
            }
        }

        "Should delete a user" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    result <- users.delete(Mailer.email)
                    maybeUser <- sql"select * from users where email = ${Mailer.email}"
                                .query[User]
                                .option
                                .transact(xa)

                } yield (result, maybeUser)

                program.asserting {
                    case (result, maybeUser) =>
                      result shouldBe true
                      maybeUser shouldBe None
                }
            }
        }

        "Should NOT delete a user that does not exist" in {
            transactor.use { xa =>
                val program = for {
                    users <- LiveUsers[IO](xa)
                    result <- users.delete("notfound@mailinator.com")
                } yield result

                program.asserting(_ shouldBe false)
            }
        }
    }
}
