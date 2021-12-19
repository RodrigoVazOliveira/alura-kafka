# Objeto:
    treinar conceitos passados no curso do arula sobre Kafka,
    usando diversos conceitos do kafka, como: levnatar vários brokers, 
    partitions, replicação, logs, entre outros.

# Tecnologias utilizadas:
    - Java 17
    - Jetty (servidor HTTP) 
    - Kafka/zookeeper
    - Gson (para conversão de JSON)
    - SQLite3 (para persistencia de dados)


# Modulos:

O projeto consiste nos modulos:
1. common-kafka:
   1. Possui um producer e um consumer generalizado para trabalhar com o kafka
2. service-email:
   1. modulo que envia 'email' para um tópico especifico com um producer.
3. service-fraud-detector:
   1. Valida uma nova ordem que é obtida por um consumer
   2. envia para um tópico de aprovado ou rejeitado
4. service-httecommerce:
   1. servidor HTTP
   2. Possui um servlet para criar uma nova ordem e enviar email com um producer
5. service-log:
   1. recebe todos os tópicos como um serviço de 'log'
6. service-new-order:
   1. gera uma nova ordem
   2. gera um novo email
7. services-users:
   1. verifica se email já existe na base de dados
   2. se não existir, ele persiste o dados
   3. usa o banco SQLITE3


# Estrutura do projeto:

~~~
.
├── common-kafka
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── dev
│   │   │   │       └── rvz
│   │   │   │           ├── config
│   │   │   │           │   ├── GsonDeserializer.java
│   │   │   │           │   └── GsonSerializer.java
│   │   │   │           └── services
│   │   │   │               ├── ConsumerFunction.java
│   │   │   │               ├── KafkaDispatcher.java
│   │   │   │               └── KafkaService.java
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           ├── config
│       │           │   ├── GsonDeserializer.class
│       │           │   └── GsonSerializer.class
│       │           └── services
│       │               ├── ConsumerFunction.class
│       │               ├── KafkaDispatcher.class
│       │               └── KafkaService.class
│       ├── common-kafka-1.0-SNAPSHOT.jar
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       └── maven-status
│           └── maven-compiler-plugin
│               ├── compile
│               │   └── default-compile
│               │       ├── createdFiles.lst
│               │       └── inputFiles.lst
│               └── testCompile
│                   └── default-testCompile
│                       └── inputFiles.lst
├── configKafka
│   ├── server1.properties
│   ├── server2.properties
│   ├── server3.properties
│   └── server4.properties
├── pom.xml
├── README.md
├── service-email
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── dev
│   │   │   │       └── rvz
│   │   │   │           ├── consummers
│   │   │   │           │   └── EmailService.java
│   │   │   │           └── models
│   │   │   │               └── deserializers
│   │   │   │                   └── Email.java
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           ├── consummers
│       │           │   └── EmailService.class
│       │           └── models
│       │               └── deserializers
│       │                   └── Email.class
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       │       ├── createdFiles.lst
│       │       │       └── inputFiles.lst
│       │       └── testCompile
│       │           └── default-testCompile
│       │               └── inputFiles.lst
│       └── service-email-1.0-SNAPSHOT.jar
├── service-fraud-detector
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── dev
│   │   │   │       └── rvz
│   │   │   │           ├── consummers
│   │   │   │           │   └── FraudDetectorService.java
│   │   │   │           └── models
│   │   │   │               └── serializables
│   │   │   │                   └── Order.java
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           ├── consummers
│       │           │   └── FraudDetectorService.class
│       │           └── models
│       │               └── serializables
│       │                   └── Order.class
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       │       ├── createdFiles.lst
│       │       │       └── inputFiles.lst
│       │       └── testCompile
│       │           └── default-testCompile
│       │               └── inputFiles.lst
│       └── service-fraud-detector-1.0-SNAPSHOT.jar
├── service-httpecommerce
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       └── java
│   │           └── dev
│   │               └── rvz
│   │                   ├── HttpEcommerce.java
│   │                   ├── models
│   │                   │   ├── deserializers
│   │                   │   │   └── Email.java
│   │                   │   └── serializables
│   │                   │       └── Order.java
│   │                   └── NewOrderServlet.java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           ├── HttpEcommerce.class
│       │           ├── models
│       │           │   ├── deserializers
│       │           │   │   └── Email.class
│       │           │   └── serializables
│       │           │       └── Order.class
│       │           └── NewOrderServlet.class
│       └── generated-sources
│           └── annotations
├── service-log
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── dev
│   │   │   │       └── rvz
│   │   │   │           └── consummers
│   │   │   │               └── LogService.java
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           └── consummers
│       │               └── LogService.class
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       │       ├── createdFiles.lst
│       │       │       └── inputFiles.lst
│       │       └── testCompile
│       │           └── default-testCompile
│       │               └── inputFiles.lst
│       └── service-log-1.0-SNAPSHOT.jar
├── service-new-order
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── dev
│   │   │   │       └── rvz
│   │   │   │           ├── models
│   │   │   │           │   ├── deserializers
│   │   │   │           │   │   └── Email.java
│   │   │   │           │   └── serializables
│   │   │   │           │       └── Order.java
│   │   │   │           └── producers
│   │   │   │               └── NewOrderMain.java
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           ├── models
│       │           │   ├── deserializers
│       │           │   │   └── Email.class
│       │           │   └── serializables
│       │           │       └── Order.class
│       │           └── producers
│       │               └── NewOrderMain.class
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       │       ├── createdFiles.lst
│       │       │       └── inputFiles.lst
│       │       └── testCompile
│       │           └── default-testCompile
│       │               └── inputFiles.lst
│       └── service-new-order-1.0-SNAPSHOT.jar
├── service-users
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── dev
│   │   │   │       └── rvz
│   │   │   │           ├── CreateUserService.java
│   │   │   │           └── models
│   │   │   │               └── serializables
│   │   │   │                   └── Order.java
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       │   └── dev
│       │       └── rvz
│       │           ├── CreateUserService.class
│       │           └── models
│       │               └── serializables
│       │                   └── Order.class
│       ├── generated-sources
│       │   └── annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       │       ├── createdFiles.lst
│       │       │       └── inputFiles.lst
│       │       └── testCompile
│       │           └── default-testCompile
│       │               └── inputFiles.lst
│       └── service-users-1.0-SNAPSHOT.jar
├── src
│   ├── main
│   │   └── resources
│   └── test
│       └── java

~~~




# commandos do kafka

alterar tamanho do particionamento:
~~~bash
$  bin/kafka-topics.sh --alter --bootstrap-server localhost:2181  --topic ECOMMERCE_NEW_ORDER --partitions 3
~~~

listar tópicos:
~~~bash
$ bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe
~~~

listar consumidores por grupo:

~~~bash
$ bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --all-groups --describe
~~~

listar os tópicos:
~~~bash
$ bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe
~~~