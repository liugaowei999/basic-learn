package activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Receiver {

    public static void main(String[] args) {
        
        // ConnectionFactory : 连接工厂， JMS用它创建链接
        ConnectionFactory connectionFactory;
        
        // Connection : JMS 客户端到JMS Provider的链接
        Connection connection = null;
        
        // Session ： 一个发送或接收消息的线程
        Session session;
        
        // Destination : 消息的目的地， 消息发送给谁
        Destination destination;
        
        // MessageConsumer : 消息消费者， 消息的接收者
        MessageConsumer consumer ;
        
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, 
                            ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.213.148:61616");
        try {
            // 从工厂构造链接对象
            connection = connectionFactory.createConnection();
            
            // 启动
            connection.start();
            
            // 获取操作链接
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            
            // 获取session注意参数值 
            destination = session.createQueue("FirstQueue");
            consumer = session.createConsumer(destination);
            
            while (true) {
                // 设置接收者接收消息的时间，为了便于测试，这里设定为100s
                TextMessage message = (TextMessage) consumer.receive(100000);
                if (null != message) {
                    System.out.println("收到消息：" + message.getText());
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
