error id: 7DD9055F965E051D9082238A166A966B
file://<WORKSPACE>/src/main/scala/com/rockthejvm/foundations/CatsEffect.scala
### java.lang.AssertionError: assertion failed

occurred in the presentation compiler.



action parameters:
offset: 375
uri: file://<WORKSPACE>/src/main/scala/com/rockthejvm/foundations/CatsEffect.scala
text:
```scala
package com.rockthejvm.foundations

import cats.{Defer, MonadError}
import cats.effect.kernel.{Deferred, Fiber}
import cats.effect.{Concurrent, GenSpawn, IO, IOApp, MonadCancel, Ref, Resource, Spawn, Sync}

import java.io.{File, FileWriter, PrintWriter}
import scala.concurrent.ExecutionContext
import scala.io.StdIn
import scala.concurrent.duration.*
import scala.util.Rando@@m

object CatsEffect extends IOApp.Simple {
  /*
    describing computations as values
   */

  // IO - describing arbitrary computations (including side effects)

  val firstIO: IO[Int] = IO.pure(42)
  val delayedIO: IO[Int] = IO {
    // complex code
    println("I'm just about to produce the meaning of life")
    42
  }

  def evaluateIO[A](io: IO[A]): Unit = {
    import cats.effect.unsafe.implicits.global // "platform"
    val meaningOfLife = io.unsafeRunSync()
    println(s"the result of the effect is: $meaningOfLife")
  }

  // transformations
  // map - flatMap
  val improvedMeaningOfLife = firstIO.map(_ * 2)
  val printedMeaningOfLife = firstIO.flatMap(mol => IO(println(mol)))
  // for-comprehensions
  def smallProgram(): IO[Unit] = for {
    line1 <- IO(StdIn.readLine())
    line2 <- IO(StdIn.readLine())
    _ <- IO(println(line1 + line2))
  } yield ()

  // old style of standard Scala apps
  // def main(args: Array[String]): Unit = {
  //   evaluateIO(smallProgram())
  // }

  // raise/"catch" errors
  val aFailure: IO[Int] = IO.raiseError(new RuntimeException("a proper failure"))
  val dealWithIt = aFailure.handleErrorWith {
    case _: RuntimeException => IO(println("Handling error here"))
  }

  // fibers - "lightweight threads"
  val delayedPrint = IO.sleep(1.second) >> IO(println(Random.nextInt(100)))
  val manyPrints = for {
    fib1 <- delayedPrint.start
    fib2 <- delayedPrint.start
    _ <- fib1.join
    _ <- fib2.join
  } yield ()

  val cancelledFiber = for {
    fib <- delayedPrint.onCancel(IO(println("I'm cancelled!"))).start
    _ <- IO.sleep(500.millis) *> IO(println("cancelling fiber")) *> fib.cancel
    _ <- fib.join
  } yield ()

  // uncancellation
  val ignoredCancelation = for {
    fib <- IO.uncancelable(_ => delayedPrint.onCancel(IO(println("I'm cancelled!")))).start
    _ <- IO.sleep(500.millis) *> IO(println("cancelling fiber")) *> fib.cancel
    _ <- fib.join
  } yield ()

  // resources
  val readingResource = Resource.make(
    IO(scala.io.Source.fromFile("src/main/scala/com/rockthejvm/foundations/CatsEffect.scala"))
  )(source => IO(println("closing source")) *> IO(source.close()))
  val readingEffect = readingResource.use(source => IO(source.getLines().foreach(println)))

  // compose resources
  val copiedFileResource = Resource.make(
    IO(new PrintWriter(new FileWriter(new File("src/main/resources/dumpedFile.scala"))))
  )(writer => IO(println("closing duplicated file")) *> IO(writer.close()))
  val compositeResource = for {
    source <- readingResource
    destination <- copiedFileResource
  } yield (source, destination)

  val copyFileEffect = compositeResource.use {
    case (source, destination) => IO(source.getLines().foreach(destination.println))
  }

  // abstract kinds of computations
  // MonadCancel = cancelable computations
  trait MyMonadCancel[F[_], E] extends MonadError[F, E] {
    trait CancellationFlagResetter {
      def apply[A](fa: F[A]): F[A] // with the cancellation flag reset
    }

    def canceled: F[Unit]
    def uncancelable[A](poll: CancellationFlagResetter => F[A]): F[A]
  }

  // monadcancel for IO
  val monadCancelIO: MonadCancel[IO, Throwable] = MonadCancel[IO]
  val uncancelableIO = monadCancelIO.uncancelable(_ => IO(42)) // same as IO.uncanlcelable(...)

  // Spawn - ability to create fibers
  trait MyGenSpawn[F[_], E] extends MonadCancel[F, E] {
    def start[A](fa: F[A]): F[Fiber[F, E, A]] // creates a fiber
    // never, cede, racepair
  }

  trait MySpawn[F[_]] extends GenSpawn[F, Throwable]

  val spawnIO = Spawn[IO]
  val fiber = spawnIO.start(delayedIO) // creates a fiber, sames as delayedIO.start

  // Concurrent = concurrency primitives (atomic references + promises)
  trait MyConcurrent[F[_]] extends Spawn[F] {
    def ref[A](a: A): F[Ref[F, A]]
    def deferred[A]: F[Deferred[F, A]]
  }

  // Temporal = ability to suspend computations for a given time
  trait MyTemporal[F[_]] extends Concurrent[F] {
    def sleep(time: FiniteDuration): F[Unit]
  }

  // Sync = ability to extend synchronous arbitrary expressions in an effect
  trait MySync[F[_]] extends MonadCancel[F, Throwable] with Defer[F] {
    def delay[A](expression: => A): F[A]
    def blocking[A](expression: => A): F[A] // runs on a dedicated blocking thread pool
  }

  // Async = ability to suspend asynchronous computations (i.e. on other thread pools) into an effect managed by CE
  trait MyAsync[F[_]] extends Sync[F] {
    def executionContext: F[ExecutionContext]
    def async[A](cb: (Either[Throwable, A] => Unit) => F[Option[F[Unit]]]): F[A]
  }


  // CE apps have a "run" method returning an IO, which will internally evaluated in a main function
  override def run: IO[Unit] = copyFileEffect
}

```


presentation compiler configuration:
Scala version: 3.8.2-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.bloop/server/bloop-bsp-clients-classes/classes-Metals-RQixn2ScQXWpEcNXMHNzCQ== [exists ], <HOME>/.cache/bloop/semanticdb/com.sourcegraph.semanticdb-javac.0.11.2/semanticdb-javac-0.11.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.2/scala3-library_3-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-effect_3/3.3.14/cats-effect_3-3.3.14.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-dsl_3/0.23.15/http4s-dsl_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-ember-server_3/0.23.15/http4s-ember-server_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-circe_3/0.23.15/http4s-circe_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-generic_3/0.14.1/circe-generic_3-0.14.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-fs2_3/0.14.0/circe-fs2_3-0.14.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-core_3/1.0.0-RC1/doobie-core_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-hikari_3/1.0.0-RC1/doobie-hikari_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-postgres_3/1.0.0-RC1/doobie-postgres_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/pureconfig/pureconfig-core_3/0.17.1/pureconfig-core_3-0.17.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/log4cats-slf4j_3/2.4.0/log4cats-slf4j_3-2.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.0/slf4j-simple-2.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-http4s_3/0.4.0/tsec-http4s_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.2/scala-library-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-effect-kernel_3/3.3.14/cats-effect-kernel_3-3.3.14.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-effect-std_3/3.3.14/cats-effect-std_3-3.3.14.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-core_3/0.23.15/http4s-core_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-ember-core_3/0.23.15/http4s-ember-core_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-server_3/0.23.15/http4s-server_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-jawn_3/0.23.15/http4s-jawn_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-core_3/0.14.2/circe-core_3-0.14.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-jawn_3/0.14.2/circe-jawn_3-0.14.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/co/fs2/fs2-core_3/3.2.12/fs2-core_3-3.2.12.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/jawn-parser_3/1.4.0/jawn-parser_3-1.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-free_3/1.0.0-RC1/doobie-free_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-collection-compat_3/2.4.4/scala-collection-compat_3-2.4.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/typename_3/1.0.0/typename_3-1.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.0/slf4j-api-2.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/co/fs2/fs2-io_3/3.2.12/fs2-io_3-3.2.12.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/postgresql/postgresql/42.2.23/postgresql-42.2.23.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/config/1.4.1/config-1.4.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/log4cats-core_3/2.4.0/log4cats-core_3-2.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-common_3/0.4.0/tsec-common_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-cipher-jca_3/0.4.0/tsec-cipher-jca_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-mac_3/0.4.0/tsec-mac_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-hash-jca_3/0.4.0/tsec-hash-jca_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-password_3/0.4.0/tsec-password_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-jwt-mac_3/0.4.0/tsec-jwt-mac_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-core_3/2.8.0/cats-core_3-2.8.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/commons-codec/commons-codec/1.15/commons-codec-1.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-parser_3/0.14.1/circe-parser_3-0.14.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lambdaworks/scrypt/1.4.0/scrypt-1.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/log4s/log4s_3/1.10.0/log4s_3-1.10.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/case-insensitive_3/1.2.0/case-insensitive_3-1.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-parse_3/0.3.8/cats-parse_3-0.3.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-crypto_3/0.2.3/http4s-crypto_3-0.2.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/comcast/ip4s-core_3/3.1.3/ip4s-core_3-3.1.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/literally_3/1.1.0/literally_3-1.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scodec/scodec-bits_3/1.1.34/scodec-bits_3-1.1.34.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/vault_3/3.2.1/vault_3-3.2.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/twitter/hpack/1.0.2/hpack-1.0.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/jawn-fs2_3/2.2.0/jawn-fs2_3-2.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-numbers_3/0.14.2/circe-numbers_3-0.14.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-free_3/2.6.1/cats-free_3-2.6.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/checkerframework/checker-qual/3.5.0/checker-qual-3.5.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-cipher-core_3/0.4.0/tsec-cipher-core_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-jwt-core_3/0.4.0/tsec-jwt-core_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-kernel_3/2.8.0/cats-kernel_3-2.8.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/simulacrum-scalafix-annotations_3/0.5.4/simulacrum-scalafix-annotations_3-0.5.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-signatures_3/0.4.0/tsec-signatures_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-bouncy_3/0.4.0/tsec-bouncy_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.68/bcprov-jdk15on-1.68.jar [exists ], <WORKSPACE>/.bloop/server/bloop-bsp-clients-classes/classes-Metals-RQixn2ScQXWpEcNXMHNzCQ==/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -Ywith-best-effort-tasty




#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:13)
	dotty.tools.dotc.core.TypeOps$.dominators$1(TypeOps.scala:248)
	dotty.tools.dotc.core.TypeOps$.approximateOr$1(TypeOps.scala:392)
	dotty.tools.dotc.core.TypeOps$.orDominator(TypeOps.scala:410)
	dotty.tools.dotc.core.Types$OrType.join(Types.scala:3691)
	dotty.tools.dotc.core.Types$OrType.widenUnionWithoutNull(Types.scala:3707)
	dotty.tools.dotc.core.Types$Type.widenUnion(Types.scala:1464)
	dotty.tools.dotc.core.ConstraintHandling.widenOr$1(ConstraintHandling.scala:676)
	dotty.tools.dotc.core.ConstraintHandling.widenInferred(ConstraintHandling.scala:697)
	dotty.tools.dotc.core.ConstraintHandling.widenInferred$(ConstraintHandling.scala:29)
	dotty.tools.dotc.core.TypeComparer.widenInferred(TypeComparer.scala:34)
	dotty.tools.dotc.core.ConstraintHandling.instanceType(ConstraintHandling.scala:738)
	dotty.tools.dotc.core.ConstraintHandling.instanceType$(ConstraintHandling.scala:29)
	dotty.tools.dotc.core.TypeComparer.instanceType(TypeComparer.scala:34)
	dotty.tools.dotc.core.TypeComparer$.instanceType(TypeComparer.scala:3516)
	dotty.tools.dotc.core.Types$TypeVar.typeToInstantiateWith(Types.scala:5164)
	dotty.tools.dotc.core.Types$TypeVar.instantiate(Types.scala:5174)
	dotty.tools.dotc.typer.Inferencing.tryInstantiate$1(Inferencing.scala:830)
	dotty.tools.dotc.typer.Inferencing.doInstantiate$1(Inferencing.scala:833)
	dotty.tools.dotc.typer.Inferencing.instantiateTypeVars(Inferencing.scala:836)
	dotty.tools.dotc.typer.Inferencing.instantiateTypeVars$(Inferencing.scala:639)
	dotty.tools.dotc.typer.Typer.instantiateTypeVars(Typer.scala:163)
	dotty.tools.dotc.typer.Inferencing.interpolateTypeVars(Inferencing.scala:707)
	dotty.tools.dotc.typer.Inferencing.interpolateTypeVars$(Inferencing.scala:639)
	dotty.tools.dotc.typer.Typer.interpolateTypeVars(Typer.scala:163)
	dotty.tools.dotc.typer.Typer.simplify(Typer.scala:3896)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3881)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3965)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3970)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:4082)
	dotty.tools.dotc.typer.Namer.typedAheadExpr$$anonfun$1(Namer.scala:1816)
	dotty.tools.dotc.typer.Namer.typedAhead(Namer.scala:1806)
	dotty.tools.dotc.typer.Namer.typedAheadExpr(Namer.scala:1816)
	dotty.tools.dotc.typer.Namer.typedAheadRhs$1$$anonfun$1(Namer.scala:2203)
	dotty.tools.dotc.inlines.PrepareInlineable$.dropInlineIfError(PrepareInlineable.scala:256)
	dotty.tools.dotc.typer.Namer.typedAheadRhs$1(Namer.scala:2203)
	dotty.tools.dotc.typer.Namer.rhsType$1(Namer.scala:2211)
	dotty.tools.dotc.typer.Namer.cookedRhsType$1(Namer.scala:2241)
	dotty.tools.dotc.typer.Namer.lhsType$1(Namer.scala:2242)
	dotty.tools.dotc.typer.Namer.inferredResultType(Namer.scala:2254)
	dotty.tools.dotc.typer.Namer.inferredType$1(Namer.scala:1859)
	dotty.tools.dotc.typer.Namer.valOrDefDefSig(Namer.scala:1865)
	dotty.tools.dotc.typer.Namer$Completer.typeSig(Namer.scala:837)
	dotty.tools.dotc.typer.Namer$Completer.completeInCreationContext(Namer.scala:1011)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:874)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:175)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:190)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:192)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:403)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:3731)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3756)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3877)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3965)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3970)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3992)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:4039)
	dotty.tools.dotc.typer.Typer.typedClassDef(Typer.scala:3433)
	dotty.tools.dotc.typer.Typer.typedTypeOrClassDef$1(Typer.scala:3772)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3776)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3877)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3965)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3970)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3992)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:4039)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:3568)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3818)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3878)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3965)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3970)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:4082)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:47)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:533)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:53)
	dotty.tools.dotc.typer.TyperPhase.$anonfun$4(TyperPhase.scala:99)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:495)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:599)
	scala.collection.immutable.List.prependedAll(List.scala:156)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:911)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:98)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:380)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1324)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:373)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1$$anonfun$2(Run.scala:420)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1$$anonfun$adapted$1(Run.scala:420)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.Run.showProgress(Run.scala:482)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:420)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:432)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:432)
	dotty.tools.dotc.Run.compileSources(Run.scala:319)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:165)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:44)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.WithSymbolSearchCollector.<init>(PcCollector.scala:346)
	dotty.tools.pc.PcDocumentHighlightProvider.<init>(PcDocumentHighlightProvider.scala:17)
	dotty.tools.pc.ScalaPresentationCompiler.documentHighlight$$anonfun$1(ScalaPresentationCompiler.scala:228)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

java.lang.AssertionError: assertion failed