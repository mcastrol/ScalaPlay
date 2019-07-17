name := "ScalaPlay"

version := "0.1"

scalaVersion := "2.11.0"

val sparkVersion = "2.1.0"

libraryDependencies ++= Seq(
//  "org.apache.spark" %% "spark-core" % sparkVersion exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,


  "org.scalikejdbc" %% "scalikejdbc"        % "3.3.5",
  "org.scalikejdbc" %% "scalikejdbc-config"  % "3.3.5",
  "org.scalikejdbc" %% "scalikejdbc-test"   % "3.3.5"   % "test",
  "com.h2database"  %  "h2"                 % "1.4.199",
  "ch.qos.logback"  %  "logback-classic"    % "1.2.3",

  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test,

  "org.postgresql" % "postgresql" % "42.1.1"
  
)


enablePlugins(ScalikejdbcPlugin)