package com.rockthejvm.foundations

object Cats {

  /*
    type classes
    - Applicative
    - Functor
    - FlatMap
    - Monad
    - ApplicativeError/MonadError
   */

  // functor - "mappable" structures
  trait MyFunctor[F[_]] {
    def map[A, B](initialValue: F[A])(f: A => B): F[A]
  }

  import cats.Functor
  import cats.instances.list.*
  val listFunctor = Functor[List]
  val mappedList = listFunctor.map(List(1, 2, 3))(_ + 1)

  // generalizable "mappable" APIs
  def increment[F[_]](container: F[Int])(using functor: Functor[F]): F[Int] =
    functor.map(container)(_ + 1)

  def main(args: Array[String]): Unit = {

  }
}
