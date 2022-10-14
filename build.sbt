scalaVersion := "3.1.3"
organization := "dev.zio"
name         := "zio-quickstart-restful-webservice"

libraryDependencies ++= Seq(
  "dev.zio"       %% "zio"            % "2.0.2",
  "dev.zio"       %% "zio-json"       % "0.3.0",
  "io.d11"        %% "zhttp"          % "2.0.0-RC11",
  "io.getquill"   %% "quill-zio"      % "4.6.0",
  "io.getquill"   %% "quill-jdbc-zio" % "4.6.0",
  "com.h2database" % "h2"             % "2.1.214",
  "ch.qos.logback" % "logback-core" %  "1.4.0",
  "ch.qos.logback" % "logback-classic" %  "1.4.0",

)
