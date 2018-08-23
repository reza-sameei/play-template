
enablePlugins(PlayScala)

scriptClasspath += "/../conf/"

packageName in Universal := s"${name.value}-${version.value}"

executableScriptName := "launcher"

makeBatScripts := Seq.empty

libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play-json" % "2.6.6"
    ,   "com.datastax.cassandra" % "cassandra-driver-core" % "3.5.1"
    ,   "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
)

// routesGenerator := InjectedRoutesGenerator
