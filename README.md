# RabbitMq消息队列笔记

## 基本知识点

采用Erlang语言实现的**Advanced Message Queued Protocol** 的消息队列，最初源于金融系统，用户在分布式系统中存储转发消息，目前用户各类系统的解耦，削峰。

### 特点

* 可靠性：通过支持消息持久化，支持事物，支持消费和传输的ack等来确保可靠性。
* 路由机制：支持主流的订阅消费模式，如广播，订阅，headers匹配等。
* 扩展性：多个RabbitMq节点可以组成一个集群，也可以根据实际业务情况动态的扩展集群中的节点。
* 高可用性：RabbitMq除了支持原生的AMQP协议，还支持STOMP，MQTT等多种消息中间件协议。
* 多语言客户端：RabbitMq几乎支持所有常用语言，如Java，Python，Ruby等。
* 管理界面：RabbitMq提供了一个简单易用的界面，使得用户可以监控和管理消息，集群中的节点。
* 插件机制：RabbitMq提供了多种插件，以实现从多方面进行扩展，当然也可以编写自己的插件。

### 基本概念

#### Message消息

具体的消息，包涵消息头（附属的配置信息）和消息体（消息的实体内容），由发布者将消息推送到**Exchange（交换机）**，由消费者从Queue中获取。

#### Publisher生产者

消息生产者，负责将消息发布到**Exchange（交换机）**中。

#### Exchange交换机

交换机，用来接收**Publisher（生产者）**发送的消息并将这些消息路由给服务器中的**Queue（队列）**。

#### Binding绑定

绑定，用于给**Exchange（交换机）**和**Queue（队列）**建立关系，从而决定将这个**Exchange（交换机**）中的哪些消息，发送到对应的**Queue（队列）**。

#### Queue队列

消息队列，用于保存消息直接发送到消费者。它是消息的容器，也是消息的终点。一个消息可投入一个或多个消息队列，消息一直在队列里面，等待消费者连接到这个队列将其取走。

#### Connection连接

连接，内部持有一些**channel（通道）**，用于和**queue（队列）**打交道。

#### Channel通道

通道，RabbitMq与外部打交道都是通过**Channel（通道）**来的，发布消息，订阅队列还是接收消息，这些动作都是通过通道来的，简单的说消息通过**Channel（通道）**塞进队列或者流出队列。

#### Consumer消费者

消费者，从消息队列中获取消息的主体。

#### Virtual Host虚拟主机

虚拟主机，表示一批交换机，消息队列，和相关对象。虚拟主机是共享相同身份认证和加密环境的独立服务器域。

每个vhost本质上就是一个mini版的RabbitMq服务器，拥有自己的队列，交换器，绑定和权限机制。

vhost是AMQP概念的基础，必须在连接时指定，RabbitMq默认的vhost是/，**可以理解为db中的数据库概念，用于逻辑拆分。**

#### Broker消息队列服务器实体

broker是指一个或多个erlang node的逻辑分组，且node上运行着RabbitMq应用程序，cluster是在broker的基础上，增加了node之间的的共享元数据的约束。

### 常用命令

* 启动服务

rabbitmq-server start &

* 停止服务

rabbitmq-server stop_  app

* 管理插件

rabbitmq-plugins enable rabbitmq_management

## 消息的生产与消费

### 基本构建缺一不可

* ConnectionFactory: 获取连接工厂。
* Connection：一个连接。
* Channel：数据通信信道，可发送和接收消息。
* Queue：具体的消息存储队列。
* Publisher：生产者。
* Consumer：消费者。

