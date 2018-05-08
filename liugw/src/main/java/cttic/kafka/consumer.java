package cttic.kafka;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

enum Type {
    AUTOCOMMIT, // 自动提交偏移量
    MANUALCOMMIT // 手动提交偏移量
}

public class consumer {
    public Properties props = null;
    public KafkaConsumer<String, String> kafkaConsumer = null;
    final int minBatchSize = 20000;
    private boolean autoCommit = true;
    private boolean assignprt = false;
    private String m_group_id = "";
    private String m_servers = "";
    private String m_topicnames = "";
    private String log_mark = "C1";
    private boolean testStopFlag = false;

    public void set_log_mark(String mark) {
        log_mark = mark;
    }

    public void set_testStopFlag(boolean flag) {
        testStopFlag = flag;
    }

    public consumer(Type enumType, boolean assignprt, String group_id, String servers, String topic) {
        m_group_id = group_id;
        m_servers = servers;
        m_topicnames = topic;

        props = new Properties();
        props.put("bootstrap.servers", m_servers);
        props.put("group.id", m_group_id);
        switch (enumType) {
        case AUTOCOMMIT:
            props.put("enable.auto.commit", "true");
            break;
        case MANUALCOMMIT:
            props.put("enable.auto.commit", "false");
            autoCommit = false;
            break;

        default:
            props.put("enable.auto.commit", "true");
            break;
        }

        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer<String, String>(props);

        /// 指定partion分区， 此消费者只读取此分区的
        if (assignprt) {
            TopicPartition partition0 = new TopicPartition(m_topicnames, 0);
            TopicPartition partition1 = new TopicPartition(m_topicnames, 1);
            kafkaConsumer.assign(Arrays.asList(partition0, partition1));

            // 制定分区的偏移量
            kafkaConsumer.seek(partition0, 1);
        } else {
            kafkaConsumer.subscribe(Arrays.asList(m_topicnames));
        }
    }

    public void doWork() {
        long begin, end;
        begin = System.currentTimeMillis();
        int totalCount = 0, batch_count = 0;
        long startOffset = 0, endOffset = 0;
        String startKey = "", startValue = "", endKey = "", endValue = "";
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(10000);
            batch_count = 0;

            for (ConsumerRecord<String, String> record : records) {
                totalCount++;
                batch_count++;
                endOffset = record.offset();
                endKey = record.key();
                endValue = record.value();
                if (totalCount == 1) {
                    startOffset = endOffset;
                    startKey = endKey;
                    startValue = endValue;
                }

                System.out.printf("offset=%d, key=%s, value=%s \n", record.offset(), record.key(), record.value());
                //				break;
                if (batch_count > minBatchSize && !autoCommit) {
                    kafkaConsumer.commitSync();
                }
            }
            if (!autoCommit) {
                kafkaConsumer.commitSync();
            }
            System.out.printf(log_mark + ":Startoffset=%d, Startkey=%s, Startvalue=%s ||| ", startOffset, startKey,
                    startValue);
            System.out.printf("Endoffset  =%d, Endkey  =%s, Endvalue  =%s @@@", endOffset, endKey, endValue);
            end = System.currentTimeMillis();
            System.out.println("Consumber do_work 执行耗时:" + (end - begin) + " 豪秒, totalCount=[" + totalCount + "]");
            begin = end;
            if (totalCount > 100000 && testStopFlag) {
                System.out.println(log_mark + " exit!");
                kafkaConsumer.close();
                System.exit(0);
            }
        }
    }

    public void workAsPartition() {
        boolean running = true;
        try {
            while (running) {
                System.out.println(Long.MAX_VALUE);
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    kafkaConsumer
                            .commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }

    public static void testFile() {
        java.io.File f = new java.io.File("d:\\aaaa\\bbb\\tt.txt");
        System.out.println(f.getAbsolutePath());
        System.out.println(f.getPath());
    }

    public static void main(String[] args) {
        //		Type enumType = Type.MANUALCOMMIT;
        //		// 是否指定特定分区
        //		boolean assignprt = false;
        //		String topic_name = "my-input-topic";
        //		consumer kafkaconsm = new consumer( enumType, assignprt,"consume_test_group","192.168.1.22:9092",topic_name );
        //		kafkaconsm.set_log_mark("C1");
        //		kafkaconsm.set_testStopFlag(true);
        //		
        //		
        //		kafkaconsm.doWork();
        ////		kafkaconsm.workAsPartition();
        testFile();
    }
}
