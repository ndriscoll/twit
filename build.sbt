scalaVersion := "3.2.0"
organization := "dev.zio"
name         := "zio-quickstart-restful-webservice"

//val zioHttpLocal = RootProject(file("/home/nick/projects/zio-http"))

val zioHttpV = "0.0.3"

libraryDependencies ++= Seq(
  "dev.zio"       %% "zio"            % "2.0.3",
  "dev.zio"       %% "zio-json"       % "0.3.0",
  "dev.zio"       %% "zio-http"       % zioHttpV,
  "dev.zio"  %% "zio-metrics" % "2.0.0",
  "dev.zio" %% "zio-metrics-prometheus" % "2.0.0",
  "io.getquill"   %% "quill-zio"      % "4.6.0",
  "io.getquill"   %% "quill-jdbc-zio" % "4.6.0",
  "com.h2database" % "h2"             % "2.1.214",
  "ch.qos.logback" % "logback-core" %  "1.4.0",
  "ch.qos.logback" % "logback-classic" %  "1.4.0",
  "org.postgresql" % "postgresql" % "42.5.0"
)

reStart / javaOptions ++= Seq(
  "-Xmx4g",
  "-Xms4g",
  "-XX:LargePageSizeInBytes=1g",
  "-XX:+UseLargePages",
  "-XX:+UseG1GC",
  "-XX:+UseStringDeduplication"
)
reStart / mainClass := Some("name.driscoll.twitter.MainApp")
