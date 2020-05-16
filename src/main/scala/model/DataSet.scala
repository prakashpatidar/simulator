package model

case class DataSet(records:Array[Array[Any]])
case class Schema(recordDef: Array[PropertyDef])
case class PropertyDef(name:String,dType:String,index:Short)

