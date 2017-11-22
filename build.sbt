import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayScala

def common = Seq(
	scalaVersion := "2.11.8",
	version := "1.0",
	organization := "com.pharbers"
)

lazy val root = (project in file(".")).
	enablePlugins(PlayScala).
	settings(common: _*).
	settings(
		name := "RMSP-Service",
		fork in run := true,
		javaOptions += "-Xmx2G"
	)

routesGenerator := InjectedRoutesGenerator

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
	jdbc,
	cache,
	ws,
	"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test",
	"commons-httpclient" % "commons-httpclient" % "3.1",
	"org.mongodb" % "casbah_2.11" % "3.1.1",
	"org.apache.poi" % "poi" % "3.8",
	"org.apache.poi" % "poi-ooxml" % "3.8",
	"xerces" % "xercesImpl" % "2.11.0",
	"log4j" % "log4j" % "1.2.17",
	"com.easemob" % "rest-java-sdk" % "1.0.1",
	"com.pharbers" % "pharbers-modules" % "0.1",
	"com.pharbers" % "pharbers-message" % "0.1",
	"com.pharbers" % "mongodb-connect" % "0.1",
	"com.pharbers" % "pharbers-data-parse" % "0.1",
	"com.pharbers" % "pharbers-sercurity" % "0.1",
	"com.pharbers" % "mongodb-driver" % "0.1",
	"com.pharbers" % "mongodb-manager" % "0.1",
	"com.pharbers" % "pharbers-cli-traits" % "0.1",
	"com.pharbers" % "pharbers-pattern" % "0.1",
	"com.pharbers" % "auth-token" % "0.1",
	"com.pharbers" % "encrypt" % "0.1",
	"com.pharbers" % "http" % "0.1",
	"com.pharbers" % "errorcode" % "0.1",
	"org.apache.commons" % "commons-email" % "1.4",
	"org.specs2" %% "specs2-core" % "3.9.1" % "test"
)







