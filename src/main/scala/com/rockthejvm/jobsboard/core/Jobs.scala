package com.rockthejvm.jobsboard.core

import cats.*
import cats.implicits.*
import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.*
import doobie.util.transactor.Transactor
import java.util.UUID
import org.typelevel.log4cats.Logger

import com.rockthejvm.jobsboard.logging.syntax.*
import com.rockthejvm.jobsboard.domain.job.*
import com.rockthejvm.jobsboard.domain.pagination.*
import doobie.util.fragment.Fragment

trait Jobs[F[_]] {
  // "Algebra"
  // CRUD
  def create (ownerEmail: String, jobInfo: JobInfo): F[UUID]
  def all(): F[List[Job]]
  def all(filter: JobFilter, pagination: Pagination): F[List[Job]]
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
    country: Option[String],
    tags: Option[List[String]],
    image: Option[String],
    seniority: Option[String],
    other: Option[String]
    active: Boolean = false
 */
class LiveJobs[F[_]: MonadCancelThrow: Logger] private (xa: Transactor[F]) extends Jobs[F] {
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

  override def all(filter: JobFilter, pagination: Pagination): F[List[Job]] = {
    val selectFragment: Fragment = fr"""
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
      """

    val fromFragment: Fragment = fr"from jobs"
    val whereFragment: Fragment = Fragments.whereAndOpt(
      filter.companies.toNel.map(companies => Fragments.in(fr"company", companies)),
      filter.locations.toNel.map(locations => Fragments.in(fr"location", locations)),
      filter.countries.toNel.map(countries => Fragments.in(fr"county", countries)),
      filter.seniorities.toNel.map(seniorities => Fragments.in(fr"seniority", seniorities)),
      filter.tags.toNel.map(tags => // intersection between filter.tags and row's tags
          Fragments.or(tags.map(tag => fr"$tag=any(tags)").toList*)
        ),
        filter.maxSalary.map(salary => fr"salaryHi > $salary"),
        filter.remote.some.map(remote => fr"remote = $remote")
    )
    val paginationFragment: Fragment =
      fr"order by id limit ${pagination.limit} offset ${pagination.offset}"
    
    val statement = selectFragment |+| fromFragment |+| whereFragment |+| paginationFragment
    
    Logger[F].info(statement.toString) *>
    statement
      .query[Job]
      .to[List]
      .transact(xa)
      .logError(e => s"Failed query: ${e.getMessage}")
  }

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
      Option[String] @unchecked,         // country,
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

    def apply[F[_]: MonadCancelThrow: Logger](xa: Transactor[F]): F[LiveJobs[F]] = new LiveJobs[F](xa).pure[F]
}   