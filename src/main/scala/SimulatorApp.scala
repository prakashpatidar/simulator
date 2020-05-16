import config.ConfigReader

object SimulatorApp extends App {
  val simulatorConfig= ConfigReader.read("simulator.json")
}
