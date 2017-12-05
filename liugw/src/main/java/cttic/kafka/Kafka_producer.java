package cttic.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Hello world!
 *
 */
public class Kafka_producer 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        long begin = System.currentTimeMillis();   
        
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        
//        String topicnames = "my-partition-topic";
        String topicnames = "my-input-topic";
        for(int i = 0; i <= 1000000; i++)
        {
        	
            producer.send(new ProducerRecord<String, String>(topicnames, 
            		      Integer.toString(i), 
            		      "["+ df.format(new Date()) +"]THis kdkdfkdjflafjdlfdjslajfdsaf  fkdsajflds kfdsakf message-"+Integer.toString(i))
            		     );
//            if( i % 100000 == 0 )
//            {
//            	try 
//            	{
//					Thread.sleep(50);
//				} catch (InterruptedException e) 
//            	{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
        }

        producer.close();
        long end = System.currentTimeMillis();   

        System.out.println("FileOutputStream执行耗时:" + (end - begin) + " 豪秒");   
        System.out.println( "Hello World111!" );
    }
}
