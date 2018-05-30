package cttic.kafka;

public class consumer2 {
    public static void main(String[] args) {
        Type enumType = Type.MANUALCOMMIT;
        // 是否指定特定分区
        boolean assignprt = false;
        //		String topic_name = "my-partition-topic";
        String topic_name = "xjphonedata";
        consumer kafkaconsm = new consumer(enumType, assignprt, "llll", "117.107.169.48:9092",
                topic_name);
        kafkaconsm.set_log_mark("C2");
        kafkaconsm.set_testStopFlag(false);
        kafkaconsm.doWork();
    }
}
