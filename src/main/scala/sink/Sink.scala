package sink

import java.io.{BufferedWriter, File, FileWriter}

import config.{SimulatorConfig, SinkConfig}
import constants.Constants
import model.Schema

abstract class Sink(sinkConfig: SinkConfig) {
  def sink(records: Seq[Vector[String]], schema: Schema): Unit = {
    records.foreach(println(_))
  }
}

object Sink {
  def apply(sinkConfig: SinkConfig): Sink = {
    Constants.Channel.withName(sinkConfig.channel) match {
      case Constants.Channel.FileSink => new FileSink(sinkConfig)
    }
  }
}

class FileSink(sinkConfig: SinkConfig) extends Sink(sinkConfig) {
  val path:String = sinkConfig.options.getOrDefault(Constants.SinkConfig.OutPath, "simulator.txt")+File.separator
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
