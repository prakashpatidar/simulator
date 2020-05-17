package constants

object Constants {
  object DType extends Enumeration
  {
    type DType =Value
    val IntType:Value= Value("INT")
    val LongType:Value= Value("LONG")
    val StringType:Value= Value("STRING")
    val DoubleType:Value= Value("DOUBLE")
    val DateType:Value= Value("DATE")
  }
  object SinkConfig
  {
    val RecordSep:String = "RecordSep"
    val ValSep:String = "ValSep"
    val OutPath:String="OutPath"
    val Multiple:String="Multiple"
    val Format:String="Format"
    val Keyspace:String="Keyspace"
    val Table:String="Table"
    val Partition:String="Partition"
    val PartitionBy:String="PartitionBy"
    val KafkaKey:String="kafka.key"
    val KafkaValue:String="kafka.value"
    val KafkaFormat:String="kafka.format"
  }
  object Options
  {
    val Min:String="min"
    val Max:String="max"
    val Sets:String="sets"
    val DateFormat:String="dateFormat"
  }
  object Algo extends Enumeration
  {
    type Algo=Value
    val Rlong:Value= Value("RLONG")
    val RInt:Value=Value("RINT")
    val RStr:Value=Value("RSTR")
    val Incr:Value= Value("INCR")
    val Guid:Value=Value("GUID")
    val Set:Value=Value("SET")
    val RDate:Value=Value("RDATE")
    val RDateTime:Value=Value("RDATETIME")
    val RGeo:Value=Value("RGEO")
  }
  object Channel extends Enumeration
  {
    type Channel = Value
    val FileSink:Value= Value("FILE")
    val DfsSink:Value= Value("DFS")
    val KafkaSink:Value= Value("KAFKA")
    val CassandraSink:Value= Value("CASSANDRA")
  }
}
