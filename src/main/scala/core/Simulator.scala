package core

import java.time.LocalDate

import config.{ConfigReader, DataConfig, PropertyConfig, SimulatorConfig}
import constants.Constants
import constants.Constants.{Algo, Options}
import model.{PropertyDef, Schema}
import org.apache.logging.log4j.LogManager
import sink.Sink

import scala.util.Random

class Simulator(simulatorConfig: SimulatorConfig) {
  private val logger = LogManager.getLogger(this.getClass)

  def getSchema(dataInfo: DataConfig): Schema = {
    Schema(dataInfo.properties.map(property => PropertyDef(property.name, property.dType, property.index)))
  }

  logger.debug("building schema")
  val schema: Schema = getSchema(simulatorConfig.dataInfo)
  logger.info(s"schema:$schema")
  val sink: Sink = Sink(simulatorConfig.sinkInfo)
  logger.info(s"sink info:$sink")

  def simulate(): Unit = {
    logger.debug(s"Start simulating data...$simulatorConfig")
    logger.info(s"Load info:${simulatorConfig.loadInfo}")
    val batchCount = simulatorConfig.loadInfo.batchCount
    val batchSize = simulatorConfig.loadInfo.batchSize
    for (i <- 1L to batchCount) {
      val records = for (j <- 1L to batchSize)
        yield {
          simulateRecord(simulatorConfig.dataInfo)
        }
      sink.sink(records, schema)
      logger.info(s"Simulated batch count:$i")
    }
  }


  def simulateRecord(dataConfig: DataConfig): Vector[String] = {
    dataConfig.properties.foldLeft(Vector[String]()) { (a, b) => simulateProperty(a, b) }
  }

  def simulateProperty(vector: Vector[String], propertyConfig: PropertyConfig): Vector[String] = {
    val min = propertyConfig.options.getOrDefault(Options.Min, "0").toLong
    val max = propertyConfig.options.getOrDefault(Constants.Options.Max, Int.MaxValue + "").toLong
    val random = min + math.random() * (max - min)
    val value = Constants.Algo.withName(propertyConfig.algoType.toUpperCase) match {
      case Algo.Guid => java.util.UUID.randomUUID.toString
      case Algo.RInt => random
      case Algo.Rlong => random.toLong
      case Algo.RStr => Random.alphanumeric.take(random.toInt).mkString
      case Algo.RDate => LocalDate.ofEpochDay(random.toInt)
      case Algo.Set =>
        val sets = Seq(propertyConfig.options.getOrDefault(Constants.Options.Sets, ""))
        sets(Random.nextInt(sets.size))
      case Algo.RGeo =>getLat+","+getLong
    }
    vector :+ value.toString
  }

  private def getLat: Double = {
    val u = Random.nextDouble()
    Math.toDegrees(Math.acos(u * 2 - 1)) - 90
  }
   def getLong: Double = {
    val v = Random.nextDouble()
    360 * v - 180
  }
}


