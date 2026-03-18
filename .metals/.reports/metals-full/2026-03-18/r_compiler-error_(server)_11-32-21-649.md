error id: A3FACB53B8D2D32A18D048DDC2FAE49F
file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/http/routes/JobRoutes.scala
### java.lang.IndexOutOfBoundsException: 1

occurred in the presentation compiler.



action parameters:
offset: 522
uri: file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/http/routes/JobRoutes.scala
text:
```scala
package com.rockthejvm.jobsboard.http.routes

import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.Monad

class JobRoutes[F[_]: Monad] private extends Http4sDsl[F]{

  // POST /jobs/offset=x&limit=y {filters} // TODO add query params and filters
  private val allJobROute: HttpRoutes[F] =  HttpRoutes.of[F] {
    case POST -> Root =>
      Ok("TODO")
  }

  // GET /jobs/uuid
  private val findJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / U@@=>
      Ok("TODO")
  }

  //POST /jobs { jobInfo }
  private val createJobRoute: HttpRoutes[F] = ???

  // PUT /jobs/uuid { jobInfo }
  private val updateJobRoute: HttpRoutes[F] = ???

  // DELETE /jobs/uuid
  private val deleteJobRoute: HttpRoutes[F] = ???

  val routes = Router(
    "/jobs" -> ???
  )
}

object JobRoutes {
    def apply[F[_]: Monad] = new JobRoutes[F]
}

```


presentation compiler configuration:
Scala version: 3.8.2-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.bloop/server/bloop-bsp-clients-classes/classes-Metals-vBdua85STFKgrfdCV-86LA== [exists ], <HOME>/.cache/bloop/semanticdb/com.sourcegraph.semanticdb-javac.0.11.2/semanticdb-javac-0.11.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.2/scala3-library_3-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-effect_3/3.3.14/cats-effect_3-3.3.14.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-dsl_3/0.23.15/http4s-dsl_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-ember-server_3/0.23.15/http4s-ember-server_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-circe_3/0.23.15/http4s-circe_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-generic_3/0.14.1/circe-generic_3-0.14.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-fs2_3/0.14.0/circe-fs2_3-0.14.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-core_3/1.0.0-RC1/doobie-core_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-hikari_3/1.0.0-RC1/doobie-hikari_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-postgres_3/1.0.0-RC1/doobie-postgres_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/pureconfig/pureconfig-core_3/0.17.1/pureconfig-core_3-0.17.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/log4cats-slf4j_3/2.4.0/log4cats-slf4j_3-2.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.0/slf4j-simple-2.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-http4s_3/0.4.0/tsec-http4s_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.2/scala-library-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-effect-kernel_3/3.3.14/cats-effect-kernel_3-3.3.14.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-effect-std_3/3.3.14/cats-effect-std_3-3.3.14.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-core_3/0.23.15/http4s-core_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-ember-core_3/0.23.15/http4s-ember-core_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-server_3/0.23.15/http4s-server_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-jawn_3/0.23.15/http4s-jawn_3-0.23.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-core_3/0.14.2/circe-core_3-0.14.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-jawn_3/0.14.2/circe-jawn_3-0.14.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/co/fs2/fs2-core_3/3.2.12/fs2-core_3-3.2.12.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/jawn-parser_3/1.4.0/jawn-parser_3-1.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/doobie-free_3/1.0.0-RC1/doobie-free_3-1.0.0-RC1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-collection-compat_3/2.4.4/scala-collection-compat_3-2.4.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/tpolecat/typename_3/1.0.0/typename_3-1.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.0/slf4j-api-2.0.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/co/fs2/fs2-io_3/3.2.12/fs2-io_3-3.2.12.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/postgresql/postgresql/42.2.23/postgresql-42.2.23.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/config/1.4.1/config-1.4.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/log4cats-core_3/2.4.0/log4cats-core_3-2.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-common_3/0.4.0/tsec-common_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-cipher-jca_3/0.4.0/tsec-cipher-jca_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-mac_3/0.4.0/tsec-mac_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-hash-jca_3/0.4.0/tsec-hash-jca_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-password_3/0.4.0/tsec-password_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-jwt-mac_3/0.4.0/tsec-jwt-mac_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-core_3/2.8.0/cats-core_3-2.8.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/commons-codec/commons-codec/1.15/commons-codec-1.15.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-parser_3/0.14.1/circe-parser_3-0.14.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/lambdaworks/scrypt/1.4.0/scrypt-1.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/log4s/log4s_3/1.10.0/log4s_3-1.10.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/case-insensitive_3/1.2.0/case-insensitive_3-1.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-parse_3/0.3.8/cats-parse_3-0.3.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/http4s/http4s-crypto_3/0.2.3/http4s-crypto_3-0.2.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/comcast/ip4s-core_3/3.1.3/ip4s-core_3-3.1.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/literally_3/1.1.0/literally_3-1.1.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scodec/scodec-bits_3/1.1.34/scodec-bits_3-1.1.34.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/vault_3/3.2.1/vault_3-3.2.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/twitter/hpack/1.0.2/hpack-1.0.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/jawn-fs2_3/2.2.0/jawn-fs2_3-2.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-numbers_3/0.14.2/circe-numbers_3-0.14.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-free_3/2.6.1/cats-free_3-2.6.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/checkerframework/checker-qual/3.5.0/checker-qual-3.5.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-cipher-core_3/0.4.0/tsec-cipher-core_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-jwt-core_3/0.4.0/tsec-jwt-core_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-kernel_3/2.8.0/cats-kernel_3-2.8.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/simulacrum-scalafix-annotations_3/0.5.4/simulacrum-scalafix-annotations_3-0.5.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-signatures_3/0.4.0/tsec-signatures_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/io/github/jmcardon/tsec-bouncy_3/0.4.0/tsec-bouncy_3-0.4.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.68/bcprov-jdk15on-1.68.jar [exists ], <WORKSPACE>/.bloop/server/bloop-bsp-clients-classes/classes-Metals-vBdua85STFKgrfdCV-86LA==/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -Ywith-best-effort-tasty




#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:134)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:38)
	scala.collection.immutable.List.apply(List.scala:83)
	dotty.tools.pc.InferCompletionType$.inferType(InferExpectedType.scala:94)
	dotty.tools.pc.InferCompletionType$.inferType(InferExpectedType.scala:62)
	dotty.tools.pc.completions.Completions.advancedCompletions(Completions.scala:543)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:131)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:139)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:197)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 1