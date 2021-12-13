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