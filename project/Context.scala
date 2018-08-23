package xyz.sigmalab.privatebuildhelper

import scala.util.Try

case class Context(
    organization: String,
    originName: String,
    originVersion: String,
    scalaVersions: Seq[String] = Seq("2.12.6"),
    modulesDir: String = "module",
    debug: Boolean = false,             // ?
    fork: Boolean = true                // ?
) {
    val settings: Seq[sbt.Def.Setting[_]] = {
        val step1 = Seq(
            sbt.Keys.organization := organization,
            sbt.Keys.name := originName,
            sbt.Keys.scalacOptions ++= Seq(
                "-feature",
                "-deprecation",
                "-language:postfixOps"
            ),
            sbt.Keys.fork := fork
        )

        val step2 = scalaVersions match {
            case Nil =>
                throw new Exception("The 'scalaVersions' can't be an empty list!")
            case head :: Nil =>
                step1 ++ Seq(
                    sbt.Keys.scalaVersion := head
                )
            case head :: _ =>
                step1 ++ Seq(
                    sbt.Keys.scalaVersion := head,
                    sbt.Keys.crossScalaVersions := scalaVersions
                )
        }

        step2
    }

    def newModule(
        name: String, 
        artifact: String,
        version: String, 
        dir: String
    ): sbt.Project = {

        sbt.Project(
            name,
            sbt.file(s"${modulesDir}/${dir}")
        ).settings(
            settings:_*
        ).settings(
            sbt.Keys.name := name,
            sbt.Keys.version := version
        )
    }

    def newModule(name: String, artifact: String, version: String = null): sbt.Project = 
        newModule(name, artifact, Option(version).getOrElse(originVersion), name)

    def newModule(name: String): sbt.Project = 
        newModule(name, name, originVersion)

    def defRoot(subs: sbt.ProjectReference*): sbt.Project =
        sbt.Project("root", sbt.file("."))
            .settings(settings:_*)
            .settings(
                sbt.Keys.version := originVersion,
                sbt.Keys.name := originName
            ).aggregate(subs:_*)
 

}


