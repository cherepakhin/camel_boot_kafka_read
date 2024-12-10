[Источник camel-integration-spring-boot-kafka](https://github.com/hardikSinghBehl/camel-integration-spring-boot-kafka)

# О чем это?

Демонстрация работы Camel с Kafka.<br/>

Два проекта __producer__ и __consumer__ .<br/>
[producer](https://github.com/cherepakhin/camel_boot_kafka_read/tree/main/producer) - генерирует сообщения и отправляет в очередь Kafka.<br/>
[consumer](https://github.com/cherepakhin/camel_boot_kafka_read/tree/main/consumer) - читает сообщения из очереди Kafka.<br/>

# Запуск

Проекты запускаются раздельно скриптами run.sh в каталоге проектов.

## Проект __producer__.
Генерирует сообщения по таймеру и отправляет их в очередь Kafka "camel-integration":

````java
public class MessageScheduler extends RouteBuilder {
    ....
		from("timer:v-search-scheduler?period=5000").bean(messageBuilder).process(messageBodyLogger)
				.marshal(JsonDataFormatter.get(MessageDTO.class))
				.process(messageBodyLoggerSecond)
				.to("kafka:" + kafkaConfigurationProperties.getTopicName());
    ....
````
Использован Camel.

## Проект __consumer__.

Читаются сообщения из очереди Kafka "camel-integration". Задано в [application.properties](https://github.com/cherepakhin/camel_boot_kafka_read/blob/main/consumer/src/main/resources/application.properties):

````properties
...
# Kafka Configuration
camel.component.kafka.brokers=${BOOTSTRAP_SERVERS:http://192.168.1.20:9092}
com.behl.kafka.topic-name=camel-integration
....
````

Чтение последнего принятого сообщения:

````shell
$ http :9090/api/messages/ 
````

# Отправка сообщений вручную.

Для отладки можно использовать какой-нибудь UI инструмент или через консоль (предполагается, что дистрибутив kafka развернут в ~/tools/kafka).

Через консоль:

````shell
echo "Hello, World from Kafka" | ~/tools/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.20:9092 --topic camel-integration

cat ./producer/doc/example_message1.json | ~/tools/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.20:9092 --topic camel-integration

````

192.168.1.20:9092 - адрес Kafka сервера.

Для демонстрации отправить сообщения по одному в Kafka топик "camel-integration" (ниже использована утилита  [jq](https://www.baeldung.com/linux/jq-command-json)). ИСПОЛЬЗОВАТЬ НУЖНО ИМЕННО JQ (убирает переводы строк), т.к. "cat" не так сработает (не убирает переводы строк).

Примеры сообщений:
````shell
$ cat example1.json
{
"id": "f10e37fa-0da7-4854-a292-33948f2ce330",
"name": "Name2",
"descriptor": "Descriptor1",
"power": "Power1",
"prefix": "Prefix1",
"suffix": "Suffix1"
}
````

[Примеры использования jq](#jq_example) в конце Readme.md.

````shell
$ jq -rc . example1.json | ./kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.20:9092 --topic camel-integration


{
"id": "f10e37fa-0da7-4854-a292-33948f2ce330",
"name": "Name1",
"descriptor": "Descriptor1",
"power": "Power1",
"prefix": "Prefix1",
"suffix": "Suffix1"
}

{
"id": "f10e37fa-0da7-4854-a292-33948f2ce331",
"name": "Name2",
"descriptor": "Descriptor2",
"power": "Power2",
"prefix": "Prefix2",
"suffix": "Suffix2"
}
````

Сообщения логируются:

````shell
2024-11-18 13:30:38.164  INFO 17625 --- [ro-information]] c.b.r.processor.MessageBodyLogger        : Polled Message information: MessageDTO(id=f10e37fa-0da7-4854-a292-33948f2ce331, name=Name2, descriptor=Descriptor2, power=Power2, prefix=Prefix2, suffix=Suffix2, foundAt=null)
Hibernate: insert into message (descriptor, found_at, name, power, prefix, suffix, id) values (?, ?, ?, ?, ?, ?, ?)
````

Camel route ConsumerKafkaQueueCamelIntegrationRoute.java читает из топика camel-integration и сохраняет memory database. Прочитать сообщение через REST 

````shell
http :9090/api/messages/
[
    {
        "id": "f10e37fa-0da7-4854-a292-33948f2ce330",
        "name": "Name1",
        "description": "Description1",
    },
    {
        "id": "f10e37fa-0da7-4854-a292-33948f2ce440",
        "name": "Name2",
        "description": "Description2",
    }
]
````

Запуск:

````shell
$ export BOOTSTRAP_SERVERS=http://192.168.1.20:9092
$ ./mvnw spring-boot:run

$ http :9090/api/messages/
HTTP/1.1 200
Connection: keep-alive
Content-Type: application/json
Date: Sun, 17 Nov 2024 09:20:57 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

[]
````

<a id="jq_example"></a>
Использование __jq__

````shell
$ jq -rc . example1.json
{"id":"f10e37fa-0da7-4854-a292-33948f2ce330","name":"Name1","descriptor":"Descriptor1","power":"Power1","prefix":"Prefix1","suffix":"Suffix1"}

$ jq -rc .name example1.json
Name1

$ jq -rc .name,.power example1.json
Name1
Power1
````

Цветной вывод:
````shell
$ jq -C . example1.json
{
"id": "f10e37fa-0da7-4854-a292-33948f2ce330",
"name": "Name1",
"descriptor": "Descriptor1",
"power": "Power1",
"prefix": "Prefix1",
"suffix": "Suffix1"
}
````

# Чтение сообщения из topic Kafka

````shell
~/tools/kafka/bin/kafka-console-consumer.sh --bootstrap-server 192.168.1.20:9092 --topic camel-integration
````

# Ручное проведение теста CONSUMER

запустить consumer скриптом:

````shell
consumer$ ./run.sh
````

Отправить сообщения скриптом:

````shell
$ producer/doc/send_many_messages.sh 100
````

"100" - количество отправляемых сообщений.

Consumer примет сообщения. Прочитать принятые сообщения командой:

````shell
$ http :9090/api/messages/
````

Удалить все сообщения в базе данных:

````shell
$ http DELETE :9090/api/messages/
````

# Запуск всего комплекса

В одном терминале запустить producer:

````shell
cd producer/
./run.sh
````
в логах будет отражаться отправка сообщений:

````shell
INFO 29720 --- [earch-scheduler] r.p.v.c.s.p.processor.MessageBodyLogger  : Message information as MessageDTO: MessageDTO{id=5fbdac35-c46a-4c36-af5e-87c33082279a, name='2024-12-08 11:15:50', description='n: 28'}
````

В другом терминале запустить consumer:

````shell
cd consumer/
./run.sh
````

В логах consumer будут отражаться результаты приема сообщений:

````shell
...
INFO 29650 --- [el-integration]] route1                                   : Message received from Kafka : {"id":"228d2df0-1854-4d72-aa33-621a9618cdcd","name":"2024-12-08 11:13:45","description":"n: 3"}
INFO 29650 --- [el-integration]] r.p.v.c.s.c.p.MyMessageBodyLogger        : Polled RECEIVED MESSAGE information: MessageDTO{id=228d2df0-1854-4d72-aa33-621a9618cdcd, name='2024-12-08 11:13:45', description='n: 3'}
INFO 29650 --- [el-integration]] r.p.v.c.s.c.p.MessageDatasourceProcessor : Process body: MessageDTO{id=228d2df0-1854-4d72-aa33-621a9618cdcd, name='2024-12-08 11:13:45', description='n: 3'}
INFO 29650 --- [el-integration]] r.p.v.c.s.c.p.MessageDatasourceProcessor : Body: MessageDTO{id=228d2df0-1854-4d72-aa33-621a9618cdcd, name='2024-12-08 11:13:45', description='n: 3'}
INFO 29650 --- [el-integration]] r.p.v.c.s.c.p.MessageDatasourceProcessor : After CAST:MessageDTO{id=228d2df0-1854-4d72-aa33-621a9618cdcd, name='2024-12-08 11:13:45', description='n: 3'}
INFO 29650 --- [el-integration]] r.p.v.c.s.c.p.MessageDatasourceProcessor : Save entity: MessageEntity{n=228d2df0-1854-4d72-aa33-621a9618cdcd, name='2024-12-08 11:13:45', description='n: 3'}

````

При появлении новых сообщений в topic, они автоматически будут приняты consumer и отображены в log. 

Если запустить проект __consumer__  на ноуте:

````shell
camel-integration-spring-boot-kafka$ ./run_consumer.sh
````
__И__ на ноуте клиент из дистрибутива Kafka:

````shell
vasi@vasi-note:~/tools$ ~/tools/kafka/bin/kafka-console-consumer.sh --bootstrap-server 192.168.1.20:9092 --topic camel-camel-integration
````

на сервере клиент из дистрибутива Kafka:

````shell
vasi@v:~/tools/kafka$ bin/kafka-console-consumer.sh --bootstrap-server 192.168.1.20:9092 --topic camel-integration
````
то сообщения будут приняты __ВСЕМИ__ клиентами. Но старые, уже прочитанные сообщения, на каком нибудь другом клиенте (при запуске этого нового клиента) на этом новом клиенте уже не принимаются.  Нагрузка на сам брокер при обмене копеечная, около 0 (проверено на 10000 сообщениях с 3 consumer).

При перезапуске consumer в логе появилось сообщение с указанием __offset__:

````shell
INFO 10381 --- [el-integration]] o.a.k.c.c.internals.SubscriptionState    : [Consumer clientId=consumer-fd34994d-4c49-4221-bf1a-1b32a645226e-1, groupId=fd34994d-4c49-4221-bf1a-1b32a645226e] Resetting offset for partition camel-integration-0 to position FetchPosition{offset=33313, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[46.146.232.50:9092 (id: 0 rack: null)], epoch=0}}.
````

При отправке 30 000 сообщений 

````shell
$ producer/doc/send_many_messages.sh 30000
````
с тремя consumers прием и сохранение сообщений заняло около минуты.


Полность протокол загрузки в [doc/log_30_000.txt.txt](doc/log_30_000.txt.txt)

Начало приема сообщений:

````shell
18:17:11.383  INFO 27200 --- [el-integration]] route1                                   : Message received from Kafka : {"id":"542ccaee-db40-471a-b318-a657f7e2dcb2","name":"NAME1","description":"DESCRIPTION1"}
18:17:12.011  INFO 27200 --- [el-integration]] r.p.v.c.s.c.p.MyMessageBodyLogger        : Polled RECEIVED MESSAGE information: MessageDTO{id=542ccaee-db40-471a-b318-a657f7e2dcb2, name='NAME1', description='DESCRIPTION1'}
````

Конец приема:

````shell
18:18:10.142  INFO 27200 --- [el-integration]] r.p.v.c.s.c.p.MessageDatasourceProcessor : After CAST:MessageDTO{id=3f11142a-a4c8-4ec9-bc88-0a76efecbf6b, name='NAME30000', description='DESCRIPTION30000'}
18:18:10.142  INFO 27200 --- [el-integration]] r.p.v.c.s.c.p.MessageDatasourceProcessor : Save entity: MessageEntity{n=3f11142a-a4c8-4ec9-bc88-0a76efecbf6b, name='NAME30000', description='DESCRIPTION30000'}
````


### Создание FAT jar файла

````shell
cd consumer
./mvnv package
````

запуск:

````shell
$ echo $JAVA_HOME
/usr/lib/jvm/java-17-openjdk-amd64

/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar target/consumer-0.0.1.jar
````