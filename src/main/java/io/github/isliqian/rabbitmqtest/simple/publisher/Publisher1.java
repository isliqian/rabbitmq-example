package io.github.isliqian.rabbitmqtest.simple.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Author:liqian
 * Date:2020/6/14
 * Desc:
 */
public class Publisher1 {

    private static final  String QUEUE = "rabbit_simple";

    public static void main(String args[]) throws IOException, TimeoutException {
        //创建connectionFactory,并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("111.229.247.151");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");



        //建立连接
        Connection connection = connectionFactory.newConnection();

        //创立通道
        Channel channel = connection.createChannel();

        /**
         * 声明MQ
         * 参数1：队列名
         * 参数2：数据是否持久化，若为false，服务停止或断电会丢失数据
         * 参数3：是否独占队列，若为true，只能绑定到当前队列的消费者才能接收到数据。
         * 参数4：是否自动删除队列，若为true，消费者接收响应后会删除数据。
         * 参数5：其他参数
         */
        channel.queueDeclare(QUEUE,false,false,false,null);

        //通过channel发送数据
        for (int i=0;i<5;i++){
            String msg = "Hello Mq";
            //s：交换机，s1: routingKey
            channel.basicPublish("",QUEUE,null,msg.getBytes());
        }
        //关闭连接
        channel.close();
        connection.close();


    }
}
