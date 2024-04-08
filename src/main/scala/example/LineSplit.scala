package example

import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.{KStream, ValueMapper}
import org.apache.kafka.streams.scala.Serdes

import java.util.Arrays
import java.util.Properties
import java.util.concurrent.CountDownLatch

object LineSplit {
  def main() {
    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-linesplit");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)
    val builder: StreamsBuilder = new StreamsBuilder()
    val source: KStream[String, String] = builder.stream("streams-plaintext-input")

    val words: KStream[String, String] = source.flatMapValues(new ValueMapper[String, Iterable[String]]() {
      override def apply(value: String): Iterable[String] =  {
        return value.split("\\W+")
      }
    })
    source.to("streams-pipe-output")
    val topology: Topology = builder.build()
    val streams: KafkaStreams = new KafkaStreams(topology, props)

    val latch: CountDownLatch = new CountDownLatch(1)

    Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
      override def run() {
        streams.close()
        latch.countDown()
      }
    })
    try {
      streams.start()
      latch.await()
    } catch {
      case e: Throwable => System.exit(1)
    }

    System.exit(0)
  }
}
