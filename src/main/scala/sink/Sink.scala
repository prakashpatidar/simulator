package sink

import java.io.{BufferedWriter, File, FileWriter}

import com.google.gson.Gson
import config.{SimulatorConfig, SinkConfig}
import constants.Constants
import model.Schema
import org.apache.log4j.{Level, Logger}
import org.apache.log4j.lf5.LogLevel
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, DataFrameWriter, Row, SaveMode, SparkSession}

abstract class Sink(sinkConfig: SinkConfig) {
  val path: String = sinkConfig.options.getOrDefault(Constants.SinkConfig.OutPath, "test") + File.separator
  val recordSeparator: String = sinkConfig.options.getOrDefault(Constants.SinkConfig.RecordSep, "\n")
  val valSeparator: String = sinkConfig.options.getOrDefault(Constants.SinkConfig.ValSep, "\t")
  val multiple: String = sinkConfig.options.getOrDefault(Constants.SinkConfig.Multiple, "0")

  def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    records.foreach(println(_))
  }
}

object Sink {

  def apply(sinkConfig: SinkConfig): Sink = {
    Constants.Channel.withName(sinkConfig.channel.toUpperCase) match {
      case Constants.Channel.FileSink => new FileSink(sinkConfig)
      case Constants.Channel.DfsSink => new DfsSink(sinkConfig)
      case Constants.Channel.CassandraSink=> new CassandraSink(sinkConfig)
      case Constants.Channel.KafkaSink => new KafkaSink(sinkConfig)
    }
  }
}

class FileSink(sinkConfig: SinkConfig) extends Sink(sinkConfig) {

  import java.nio.file.Files
  import java.nio.file.Paths

  Files.createDirectories(Paths.get(path))
  var bw = new BufferedWriter(new FileWriter(new File(path + System.currentTimeMillis())))

  override def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    if (multiple == "1") {
      bw.close()
      println(path + System.currentTimeMillis())
      bw = new BufferedWriter(new FileWriter(new File(path + System.currentTimeMillis())))
    }
    records.foreach(record => bw.write(record.mkString(valSeparator) + recordSeparator))
    bw.flush()
  }
}

class SparkSink(sinkConfig: SinkConfig) extends Sink(sinkConfig) {
  Logger.getLogger("org").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  val spark = SparkSession.builder().appName("Simulator").master("local").getOrCreate();
  val format = sinkConfig.options.getOrDefault(Constants.SinkConfig.Format, "org.apache.spark.sql.cassandra")
  val partition: String = sinkConfig.options.get(Constants.SinkConfig.Partition)
  val partitionBy: String = sinkConfig.options.get(Constants.SinkConfig.PartitionBy)

  override def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    val writer = getWriter(records, schema)
    if (path != null)
      writer.save(path)
    else
      writer.save()
  }

  def getWriter(records: Seq[Vector[String]], schema: Schema): DataFrameWriter[Row] = {
    val df = getDf(records, schema)
    df.show(2)
    df.write
      .format(format)
      .mode(SaveMode.Append)
      .options(sinkConfig.options)
  }

  def getDf(records: Seq[Vector[String]], schema: Schema): DataFrame = {
    val rdd: RDD[Row] = spark.sparkContext.parallelize(records.map(record => Row(record: _*)))
    val df = spark.createDataFrame(rdd, schema.getSchema)
    if (partition != null)
      df.coalesce(partition.toInt)
    df
  }


}



class DfsSink(sinkConfig: SinkConfig) extends SparkSink(sinkConfig) {
  override def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    val writer = getWriter(records, schema)
    writer.save(path)
  }

  override def getWriter(records: Seq[Vector[String]], schema: Schema): DataFrameWriter[Row] = {
    val writer = super.getWriter(records, schema)
    if (partitionBy != null) writer.partitionBy(partitionBy)
    writer
  }

  override def getDf(records: Seq[Vector[String]], schema: Schema): DataFrame = {
    val df= super.getDf(records, schema)
    if(partition!=null)
      df.coalesce(partition.toInt)
    df
  }
}

class NonPathSink(sinkConfig: SinkConfig) extends SparkSink(sinkConfig) {
  override def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    val writer = getWriter(records, schema)
    writer.save()
  }
}
class CassandraSink(sinkConfig: SinkConfig) extends NonPathSink(sinkConfig)

class KafkaSink(sinkConfig: SinkConfig) extends NonPathSink(sinkConfig) {
  val kafkaKey: String = sinkConfig.options.get(Constants.SinkConfig.KafkaKey)
  val kafkaValue: String = sinkConfig.options.get(Constants.SinkConfig.KafkaValue)
  val kafkaFormat: String = sinkConfig.options.get(Constants.SinkConfig.KafkaFormat)
  val gson = new Gson()
  import spark.implicits._
  override def getDf(records: Seq[Vector[String]], schema: Schema): DataFrame = {
    val recordsKv = records.map(record =>
      (record(schema.getColumnIndex(kafkaKey)),getValue(record)))
    recordsKv.toDF("key", "value")
  }
  def getValue(record: Vector[String]) = {
    record.mkString(valSeparator)
  }
}