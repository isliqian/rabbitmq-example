package io.github.isliqian.rabbitmqtest.topic.publisher;

import com.rabbitmq.client.BuiltinExchangeType;
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


    private static final String EX_CHANGE = "rabbit_ex_topic";

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
         * 参数1：交换机名称
         * 参数2：声明交换机类型，主要有以下类型，主要使用前三种类型
         * fanout:会把所有发送到该交换机的消息路由到所有与它绑定的Queue中
         * direct：会把消息路由到那些banding key与routing key完全匹配的Queue种
         * topic：模糊匹配，可以通过通配符满足一部分规则就可以传送，其中"*"用于匹配一个单词，"#"用于匹配多个单词（可以是零个）
         * headers：headers类型的Exchange不依赖于routing key与binding key的匹配规则来路由消息，而是根据发送消息中的headers属性进行匹配
         */
            channel.exchangeDeclare(EX_CHANGE, BuiltinExchangeType.TOPIC);

        channel.basicPublish(EX_CHANGE,"routing.single",null,"routing.single".getBytes());
        channel.basicPublish(EX_CHANGE,"routing.single.multiple",null,"routing.single.multiple".getBytes());
        channel.basicPublish(EX_CHANGE,"other.single",null,"other.single other".getBytes());
        //关闭连接
        channel.close();
        connection.close();


    }
}
