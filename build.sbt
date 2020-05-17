name := "simulator"

version := "0.1"

scalaVersion := "2.12.11"
// https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.13.3"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.13.3"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.6"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.0-preview2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.0-preview2"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.0.0-preview2"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.0-preview2"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "3.0.0-alpha2"


libraryDependencies += "joda-time" % "joda-time" % "2.10.6"


