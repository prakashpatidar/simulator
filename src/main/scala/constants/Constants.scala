package constants

object Constants {
  object DType extends Enumeration
  {
    type DType =Value
    val IntType:Value= Value("Int")
    val LongType:Value= Value("Long")
    val StringType:Value= Value("String")
  }
  object SinkConfig
  {
    val RecordSep:String = "RecordSep"
    val ValSep:String = "ValSep"
    val OutPath:String="OutPath"
    val Multiple:String="Multiple"
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
  }
}
