package config



import com.google.gson.Gson

import scala.io.Source
import org.apache.logging.log4j.LogManager


object ConfigReader {
    private val logger = LogManager.getLogger(ConfigReader.getClass)
    def read(configFileName:String):SimulatorConfig=
        {
            logger.debug(s"reading config file:$configFileName")
            val content=Source.fromResource(configFileName).mkString
            logger.info(s"config file:$configFileName, content :$content")
            val gson= new Gson()
            gson.fromJson(content,classOf[SimulatorConfig])
        }
}
