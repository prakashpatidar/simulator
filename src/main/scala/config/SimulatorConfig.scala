package config

case class SimulatorConfig(loadInfo: LoadConfig, dataInfo: DataConfig, sinkInfo: SinkConfig)
case class LoadConfig(batchCount:Long=1000, batchSize:Long=1000, timeThresold:Long=1000)
case class DataConfig(properties:Array[PropertyConfig])
case class PropertyConfig(name:String, dType:String, algoType:String,index:Short, options:java.util.HashMap[String,String])
case class SinkConfig(channel:String, options:java.util.HashMap[String,String])