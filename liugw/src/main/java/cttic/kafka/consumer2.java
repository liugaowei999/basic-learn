package cttic.kafka;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class consumer2 
{
	public static void main(String[] args)
	{
		Type enumType = Type.MANUALCOMMIT;
		// 是否指定特定分区
		boolean assignprt = false;
//		String topic_name = "my-partition-topic";
		String topic_name = "my-input-topic";
		consumer kafkaconsm = new consumer( enumType, assignprt,"consume_test_group","localhost:9092",topic_name );
		kafkaconsm.set_log_mark("C2");
		kafkaconsm.set_testStopFlag(false);
		kafkaconsm.doWork();
	}
}
