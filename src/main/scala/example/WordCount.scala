package example

import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.Serdes.{Long, String}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.common.serialization.Serdes._

import java.util.{Locale, Properties}
import java.util.concurrent.CountDownLatch
import org.apache.kafka.streams.scala.kstream.Materialized

object WordCount {
  def main(args: Array[String]) {
    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-linesplit");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    val builder: StreamsBuilder = new StreamsBuilder()
    val source = builder.stream[String, String]("streams-plaintext-input")
    //implicit val materialized = Materialized.`with`[String, Long, ByteArrayWindowStore]as("counts-store")
    val wordCounts = source.flatMapValues(value => {
      val transform = value.toLowerCase(Locale.getDefault()).split("\\W+")
      print("found something!")
      transform.toList
    })
      .groupBy((_, value) => value)
      .count()
      //.count(Materialized.[String, Long, KeyValueStore[Bytes, byte[]]]as("counts-store"))
      wordCounts
        .toStream
        .to("streams-wordcount-output")
        val topology: Topology = builder.build()
        val streams: KafkaStreams = new KafkaStreams(topology, props)

        val latch: CountDownLatch = new CountDownLatch(1)

        sys.addShutdownHook {
          streams.close()
          latch.countDown()
        }

        try {
          streams.start()
          latch.await()
        } catch {
          case e: Throwable => System.exit(1)
        }

        System.exit(0)
  }
}


