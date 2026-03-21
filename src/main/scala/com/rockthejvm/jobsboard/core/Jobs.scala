package com.rockthejvm.jobsboard.core

import cats.*
import cats.implicits.*
import cats.effect.*
import com.rockthejvm.jobsboard.domain.job.{Job, JobInfo}
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.*
import doobie.util.transactor.Transactor
import java.util.UUID

trait Jobs[F[_]] {
  // "Algebra"
  // CRUD
  def create (ownerEmail: String, jobInfo: JobInfo): F[UUID]
  def all(): F[List[Job]]
  def find(id: UUID): F[Option[Job]]
  def update(id: UUID, jobInfo: JobInfo): F[Option[Job]]
  def delete(id: UUID): F[Int]
}

/* 
    id: UUID,
    date: Long,
    ownerEmail: String,
    company: String,
    title: String,
    description: String,
    externalUrl: String,
    remote: Boolean,
    location: String,
    salaryLo: Option[Int],
    salaryHi: Option[Int],
    currency: Option[String],
    country: Option[Long],
    tags: Option[List[String]],
    image: Option[String],
    seniority: Option[String],
    other: Option[String]
    active: Boolean = false
 */
class LiveJobs[F[_]: MonadCancelThrow] private (xa: Transactor[F]) extends Jobs[F] {
  override def create (ownerEmail: String, jobInfo: JobInfo): F[UUID] =
    sql"""
      insert into jobs(
        date,
        ownerEmail,
        company,
        title,
        description,
        externalUrl,
        remote,
        location,
        salaryLo,
        salaryHi,
        currency,
        country,
        tags,
        image,
        seniority,
        other,
        active
      ) values (
        ${System.currentTimeMillis()},
        $ownerEmail,
        ${jobInfo.company},
        ${jobInfo.title},
        ${jobInfo.description},
        ${jobInfo.externalUrl},
        ${jobInfo.remote},
        ${jobInfo.location},
        ${jobInfo.salaryLo},
        ${jobInfo.salaryHi},
        ${jobInfo.currency},
        ${jobInfo.country},
        ${jobInfo.tags},
        ${jobInfo.image},
        ${jobInfo.seniority},
        ${jobInfo.other},
        false
    )
    """
    .update
    .withUniqueGeneratedKeys[UUID]("id")
    .transact(xa)

  override def all(): F[List[Job]] = 
    sql"""
      select
        id,
        date,
        ownerEmail,
        company,
        title,
        description,
        externalUrl,
        remote,
        location,
        salaryLo,
        salaryHi,
        currency,
        country,
        tags,
        image,
        seniority,
        other,
        active
      from jobs
    """
    .query[Job]
    .to[List]
    .transact(xa)

  override def find(id: UUID): F[Option[Job]] = 
    sql"""
      select
        id,
        date,
        ownerEmail,
        company,
        title,
        description,
        externalUrl,
        remote,
        location,
        salaryLo,
        salaryHi,
        currency,
        country,
        tags,
        image,
        seniority,
        other,
        active
      from jobs
      where id = $id
    """
    .query[Job]
    .option
    .transact(xa)

  override def update(id: UUID, jobInfo: JobInfo): F[Option[Job]] =
    sql"""
      update jobs
      set
        company = ${jobInfo.company},
        title = ${jobInfo.title},
        description = ${jobInfo.description},
        externalUrl = ${jobInfo.externalUrl},
        remote = ${jobInfo.remote},
        location = ${jobInfo.location},
        salaryLo = ${jobInfo.salaryLo},
        salaryHi = ${jobInfo.salaryHi},
        currency = ${jobInfo.currency},
        country = ${jobInfo.country},
        tags = ${jobInfo.tags},
        image = ${jobInfo.image},
        seniority = ${jobInfo.seniority},
        other = ${jobInfo.other}
      where id = $id
    """
    .update
    .run
    .transact(xa)
    .flatMap(_ => find(id)) // return the updated job


  override def delete(id: UUID): F[Int] = 
    sql"""
      delete from jobs
      where id = $id
    """
    .update
    .run
    .transact(xa)
}

object LiveJobs {
    given jobRead: Read[Job] = Read[(
      UUID,                 // id,
      Long,                 // date,
      String,               // ownerEmail,
      String,               // company,
      String,               // title,
      String,               // description,
      String,               // externalUrl,
      Boolean,              // remote,
      String,               // location,
      Option[Int] @unchecked,          // salaryLo,
      Option[Int] @unchecked,          // salaryHi,
      Option[String] @unchecked,       // currency,
      Option[Long] @unchecked,         // country,
      Option[List[String]] @unchecked, // tags,
      Option[String] @unchecked,       // image,
      Option[String] @unchecked,       // seniority,
      Option[String] @unchecked,       // other,
      Boolean)]             // active
    .map { case (
        id,
        date,
        ownerEmail,
        company,
        title,
        description,
        externalUrl,
        remote,
        location,
        salaryLo,
        salaryHi,
        currency,
        country,
        tags,
        image,
        seniority,
        other,
        active) => Job(
        id,
        date,
        ownerEmail,
        JobInfo(
          company,
          title,
          description,
          externalUrl,
          remote,
          location,
          salaryLo,
          salaryHi,
          currency,
          country,
          tags,
          image,
          seniority,
          other),
        active)
    }

  def apply[F[_]: MonadCancelThrow](xa: Transactor[F]): F[LiveJobs[F]] = new LiveJobs[F](xa).pure[F]
}   