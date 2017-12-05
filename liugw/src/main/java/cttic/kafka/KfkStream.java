package cttic.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

public class KfkStream 
{

	public void kafkaStream()
	{
		System.out.println("kafkaStream enter ...");
		Map<String, Object> props = new HashMap<>();
	    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
	    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
	    props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
	    StreamsConfig config = new StreamsConfig(props);

	    KStreamBuilder builder = new KStreamBuilder();
//	    builder.stream("my-input-topic1").mapValues( "5").to("my-output-topic");

	    KafkaStreams streams = new KafkaStreams(builder, config);
	    System.out.println("kafkaStream start ...");
	    streams.start();
	    System.out.println("kafkaStream end ...");
	}
	
	
	public static void main(String[] args)
	{
		KfkStream kafkaconsm = new KfkStream( );
		
		kafkaconsm.kafkaStream();
	}
}
