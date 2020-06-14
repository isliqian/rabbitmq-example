package io.github.isliqian.rabbitmqtest.routing.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Author:liqian
 * Date:2020/6/14
 * Desc:
 */
public class Consumer1 {

    private static final  String QUEUE = "rabbit_routing";

    private static final String EX_CHANGE = "rabbit_ex_routing";

    public static void main(String args[]) throws IOException, TimeoutException {
        //创建connectionFactory,并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("111.229.247.151");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        //建立连接
        Connection connection = connectionFactory.newConnection();

        //通过连接建立一个通道
        Channel channel = connection.createChannel();

        //声明一个队列
        channel.queueDeclare(QUEUE,false,false,false,null);

        //绑定交换机
        channel.exchangeBind(QUEUE,EX_CHANGE,"red");

        //创建消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException
            {
                String routingKey = envelope.getRoutingKey();
                long deliveryTag = envelope.getDeliveryTag();
                String msg = new String(body,"UTF-8");
                System.out.println("1接收消息："+msg);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //确认响应
                channel.basicAck(deliveryTag, false); //false表示确认响应
            }
        };
        //消费者监听接收消息
        while (true){
            channel.basicConsume(QUEUE,false,defaultConsumer);
        }
    }
}
