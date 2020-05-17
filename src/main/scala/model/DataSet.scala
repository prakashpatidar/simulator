package model

import constants.Constants
import constants.Constants.DType
import org.apache.spark.sql.types.{DataType, DateType, DoubleType, IntegerType, LongType, StringType, StructField, StructType}

case class DataSet(records:Array[Array[Any]])
case class Schema(recordDef: Array[PropertyDef])
{
  def getColumns:Array[String]=recordDef.map(_.name)
  def getColumnIndex(column:String):Short=
    {
      recordDef.find(_.name.equals(column)).get.index
    }
  def getType(dType: String): DataType = Constants.DType.withName(dType.toUpperCase()) match {
      case DType.StringType=>StringType
      case DType.IntType=>IntegerType
      case DType.LongType=>StringType
      case DType.DoubleType=>DoubleType
      case DType.DateType=>StringType
    }

  def getSchema:StructType=StructType(recordDef.map(x=>StructField(x.name,getType(x.dType),x.nullable)))

}
case class PropertyDef(name:String,dType:String,index:Short,nullable:Boolean=true)

