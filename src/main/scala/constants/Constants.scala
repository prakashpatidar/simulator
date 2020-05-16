package constants

object Constants {
  object DType extends Enumeration
  {
    type DType =Value
    val IntType:Value= Value("INT")
    val LongType:Value= Value("LONG")
    val StringType:Value= Value("STRING")
    val DoubleType:Value= Value("DOUBLE")
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

  }
  object Options
  {
    val Min:String="min"
    val Max:String="max"
    val Sets:String="sets"
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
    val FileSink:Value= Value("File")
    val SparkSink:Value= Value("Spark")
  }
}
