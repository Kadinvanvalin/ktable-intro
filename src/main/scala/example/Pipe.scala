package example

import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.scala.Serdes

import java.util.Properties
 object Pipe extends App {
   val props: Properties = new Properties();
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  val builder: StreamsBuilder = new StreamsBuilder()
  val source: KStream[String, String] = builder.stream("streams-plaintext-input")
  source.to("streams-pipe-output")
  val topology: Topology = builder.build()
  println(topology.describe())
}