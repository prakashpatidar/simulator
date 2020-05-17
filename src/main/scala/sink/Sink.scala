package sink

import java.io.{BufferedWriter, File, FileWriter}

import config.{SimulatorConfig, SinkConfig}
import constants.Constants
import model.Schema
import org.apache.log4j.{Level, Logger}
import org.apache.log4j.lf5.LogLevel
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SaveMode, SparkSession}

abstract class Sink(sinkConfig: SinkConfig) {

  def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    records.foreach(println(_))
  }
}

object Sink {
  def apply(sinkConfig: SinkConfig): Sink = {
    Constants.Channel.withName(sinkConfig.channel) match {
      case Constants.Channel.FileSink => new FileSink(sinkConfig)
      case Constants.Channel.SparkSink => new SparkSink(sinkConfig)
    }
  }
}

class FileSink(sinkConfig: SinkConfig) extends Sink(sinkConfig) {
  val path:String = sinkConfig.options.getOrDefault(Constants.SinkConfig.OutPath, "test")+File.separator
  val recordSeparator:String = sinkConfig.options.getOrDefault(Constants.SinkConfig.RecordSep, "\n")
  val valSeparator:String = sinkConfig.options.getOrDefault(Constants.SinkConfig.ValSep, "\t")
  val multiple:String = sinkConfig.options.getOrDefault(Constants.SinkConfig.Multiple, "0")

  import java.nio.file.Files
  import java.nio.file.Paths
  Files.createDirectories(Paths.get(path))
  var bw = new BufferedWriter(new FileWriter(new File(path+System.currentTimeMillis())))

  override def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    if (multiple == "1") {
      bw.close()
      println(path+System.currentTimeMillis())
      bw = new BufferedWriter(new FileWriter(new File(path + System.currentTimeMillis())))
    }
    records.foreach(record => bw.write(record.mkString(valSeparator) + recordSeparator))
    bw.flush()
  }
}

class SparkSink(sinkConfig: SinkConfig) extends Sink(sinkConfig)
{
  Logger.getLogger("org").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  val spark =SparkSession.builder().appName("Simulator").master("local").getOrCreate();

  val format=sinkConfig.options.getOrDefault(Constants.SinkConfig.Format,"org.apache.spark.sql.cassandra")
  override def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    import spark.implicits._
    val rdd:RDD[Row]=spark.sparkContext.parallelize(records.map(record=>Row(record:_*)))
    val df=spark.createDataFrame(rdd,schema.getSchema)

    df.printSchema()
    df.show(10)
    df.write
      .format(format)
      .mode(SaveMode.Append)
      .options(sinkConfig.options)
      .save()
  }
}
