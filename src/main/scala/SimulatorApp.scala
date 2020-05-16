import config.ConfigReader
import core.Simulator

object SimulatorApp extends App {
  val simulatorConfig= ConfigReader.read("simulator.json")
  new Simulator(simulatorConfig).simulate()
}
