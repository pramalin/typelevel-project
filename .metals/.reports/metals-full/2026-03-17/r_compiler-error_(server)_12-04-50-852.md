error id: 4B45032DCB6DC4D998F57879FA47F9B3
file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/Main.scala
### java.lang.AssertionError: assertion failed: TypeAlias(HKTypeLambda(List(F), List(TypeBounds(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Nothing),HKTypeLambda(List(_$6), List(TypeBounds(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Nothing),TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Any))), TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Any), List()))), AppliedType(TypeRef(TermRef(TermRef(ThisType(TypeRef(NoPrefix,module class org)),object http4s),object package),type Http),List(HKTypeLambda(List(_$7), List(TypeBounds(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Nothing),TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Any))), AppliedType(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class cats)),object data),OptionT),List(TypeParamRef(F), TypeParamRef(_$7)))), TypeParamRef(F)))))

occurred in the presentation compiler.



action parameters:
offset: 439
uri: file://<WORKSPACE>/src/main/scala/com/rockthejvm/jobsboard/Main.scala
text:
```scala
package com.rockthejvm.jobsboard

import cats.effect.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.*

object Application extends IOApp {

    /* 
       1 - add a plain health endpoint to our app
       2 - add minimal configuration
       3 - basic http server layout 
     */


  def healthEndPoint[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*
    HttpRoutes@@.of[F] {
      case GET -> Root / "health" => Ok("All going great!")
    }
  }

  def allROutes[F[_]: Monad]: HttpRoutes[F] = courseRoutes[F] <+> healthEndPoint[F]

  def routerWithPathPrefixes = Router(
    "/api" -> courseRoutes[IO],
    "/private" -> healthEndPoint[IO]
  ).orNotFound

  override def run: effect.IO[Unit] = EmberServerBuilder
    .default[IO]
    .withHttpApp(routerWithPathPrefixes)
    .build
    .use(_ => IO.println("Server ready") *> IO.never)

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
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:10)
	dotty.tools.dotc.core.Types$TypeBounds.<init>(Types.scala:5751)
	dotty.tools.dotc.core.Types$RealTypeBounds.<init>(Types.scala:5828)
	dotty.tools.dotc.core.Types$TypeBounds$.apply(Types.scala:5869)
	dotty.tools.dotc.core.Types$TypeBounds.derivedTypeBounds(Types.scala:5759)
	dotty.tools.dotc.core.Types$ApproximatingTypeMap.derivedTypeBounds(Types.scala:6778)
	dotty.tools.dotc.core.Types$TypeMap.mapOver(Types.scala:6479)
	dotty.tools.dotc.core.TypeOps$AsSeenFromMap.apply(TypeOps.scala:112)
	dotty.tools.dotc.core.TypeOps$AsSeenFromMap.apply(TypeOps.scala:70)
	scala.collection.immutable.List.loop$3(List.scala:466)
	scala.collection.immutable.List.mapConserve(List.scala:491)
	dotty.tools.dotc.core.Types$TypeMap.mapOverLambda(Types.scala:6363)
	dotty.tools.dotc.core.TypeOps$AsSeenFromMap.apply(TypeOps.scala:106)
	dotty.tools.dotc.core.TypeOps$.asSeenFrom(TypeOps.scala:56)
	dotty.tools.dotc.core.Types$Type.asSeenFrom(Types.scala:1192)
	dotty.tools.dotc.core.Denotations$SingleDenotation.derived$1(Denotations.scala:1112)
	dotty.tools.dotc.core.Denotations$SingleDenotation.computeAsSeenFrom(Denotations.scala:1139)
	dotty.tools.dotc.core.Denotations$SingleDenotation.computeAsSeenFrom(Denotations.scala:1092)
	dotty.tools.dotc.core.Denotations$PreDenotation.asSeenFrom(Denotations.scala:137)
	dotty.tools.dotc.core.SymDenotations$ClassDenotation.findMember(SymDenotations.scala:2233)
	dotty.tools.dotc.core.Types$Type.go$1(Types.scala:831)
	dotty.tools.dotc.core.Types$Type.findMember(Types.scala:1021)
	dotty.tools.dotc.core.Types$Type.memberBasedOnFlags(Types.scala:804)
	dotty.tools.dotc.core.Types$Type.member(Types.scala:788)
	dotty.tools.dotc.core.Types$Type.allMembers$$anonfun$1(Types.scala:1179)
	scala.runtime.function.JProcedure2.apply(JProcedure2.java:15)
	scala.runtime.function.JProcedure2.apply(JProcedure2.java:10)
	dotty.tools.dotc.core.Types$.dotty$tools$dotc$core$Types$Type$$_$memberDenots$$anonfun$1(Types.scala:1067)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.BitmapIndexedSetNode.foreach(HashSet.scala:954)
	scala.collection.immutable.BitmapIndexedSetNode.foreach(HashSet.scala:961)
	scala.collection.immutable.HashSet.foreach(HashSet.scala:187)
	dotty.tools.dotc.core.Types$Type.memberDenots(Types.scala:1067)
	dotty.tools.dotc.core.Types$Type.allMembers(Types.scala:1179)
	dotty.tools.pc.AutoImportsProvider.correctInTreeContext$1(AutoImportsProvider.scala:55)
	dotty.tools.pc.AutoImportsProvider.$anonfun$6(AutoImportsProvider.scala:119)
	scala.collection.immutable.List.noneIn$1(List.scala:509)
	scala.collection.immutable.List.filterCommon(List.scala:575)
	scala.collection.immutable.List.filter(List.scala:496)
	dotty.tools.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:118)
	dotty.tools.pc.ScalaPresentationCompiler.autoImports$$anonfun$1(ScalaPresentationCompiler.scala:323)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: TypeAlias(HKTypeLambda(List(F), List(TypeBounds(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Nothing),HKTypeLambda(List(_$6), List(TypeBounds(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Nothing),TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Any))), TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Any), List()))), AppliedType(TypeRef(TermRef(TermRef(ThisType(TypeRef(NoPrefix,module class org)),object http4s),object package),type Http),List(HKTypeLambda(List(_$7), List(TypeBounds(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Nothing),TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class <root>)),object scala),Any))), AppliedType(TypeRef(TermRef(ThisType(TypeRef(NoPrefix,module class cats)),object data),OptionT),List(TypeParamRef(F), TypeParamRef(_$7)))), TypeParamRef(F)))))