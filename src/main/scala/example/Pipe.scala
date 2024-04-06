package example

import org.apache.kafka.streams._

import java.util.Properties
 class Pipe {
 def main(): Unit = {
   val props: Properties = new Properties();
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
 }
}