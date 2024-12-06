Проект не мой.
[Источник camel-integration-spring-boot-kafka](https://github.com/hardikSinghBehl/camel-integration-spring-boot-kafka)

# Мои комметарии

Два проекта __superhero-searcher__ и __superhero-registry__ .<br/>
[superhero-searcher](https://github.com/cherepakhin/camel_boot_kafka_read/tree/main/superhero-registry) - генерирует сообщения и отправляет в очередь Kafka.<br/>
[superhero-registry](https://github.com/cherepakhin/camel_boot_kafka_read/tree/main/superhero-searcher) - читает сообщения из очереди Kafka.<br/>

Проекты запускаются раздельно скриптами run.sh в каталоге проектов.
Проект __superhero-searcher__ генерирует сообщения по таймеру и отправляет их в очередь Kafka "superhero-information":

````java
public class SuperHeroSearchScheduler extends RouteBuilder {
    ....
    public void configure() {
        from("timer:superhero-search-scheduler?period=5000").bean(superHeroSearcher).process(messageBodyLogger)
                .marshal(JsonDataFormatter.get(SuperHero.class))
                .process(messageBodyLoggerSecond)
                .to("kafka:" + kafkaConfigurationProperties.getTopicName()); // topic "superhero-information"
    }
    ....
````

В проекте __superhero-registry__ читаются сообщения из очереди Kafka "superhero-information". Задано в [application.properties](https://github.com/cherepakhin/camel_boot_kafka_read/blob/main/superhero-registry/src/main/resources/application.properties):

````properties
...
# Kafka Configuration
camel.component.kafka.brokers=${BOOTSTRAP_SERVERS:http://192.168.1.20:9092}
com.behl.kafka.topic-name=superhero-information
....
````

com.behl.kafka.topic-name=superhero-information


Чтение последнего принятого сообщения:

````shell
$ http :9090/v1/registry/superheroes" 
````

Сообщения можно отправлять вручную (описано ниже) или генерировать другой программой 
(см. [https://github.com/cherepakhin/camel_boot_kafka_read/tree/main/superhero-searcher](https://github.com/cherepakhin/camel_boot_kafka_read/tree/main/superhero-searcher))


# Отправка сообщений вручную.

Можно использовать какой-нибудь UI инструмент или через консоль (предполагается, что дистрибутив kafka развернут в ~/tools/kafka).

````shell
echo "Hello, World from Kafka" | ~/tools/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.20:9092 --topic superhero-information
````

192.168.1.20:9092 - адрес Kafka сервера.

Для демонстрации отправить сообщения по одному в Kafka топик "superhero-information" (ниже использована утилита  [jq](https://www.baeldung.com/linux/jq-command-json)). ИСПОЛЬЗОВАТЬ НУЖНО ИМЕННО JQ (убирает переводы строк), т.к. "cat" не так сработает (не убирает переводы строк).

Примеры сообщений:
````shell
$ cat example1.json
{
"id": "f10e37fa-0da7-4854-a292-33948f2ce330",
"name": "Name1",
"descriptor": "Descriptor1",
"power": "Power1",
"prefix": "Prefix1",
"suffix": "Suffix1"
}
````

[Примеры использования jq](#jq_example) в конце Readme.md.

````shell
$ jq -rc . example1.json | ./kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.20:9092 --topic superhero-information


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
2024-11-18 13:30:38.164  INFO 17625 --- [ro-information]] c.b.r.processor.MessageBodyLogger        : Polled Superhero information: SuperHero(id=f10e37fa-0da7-4854-a292-33948f2ce331, name=Name2, descriptor=Descriptor2, power=Power2, prefix=Prefix2, suffix=Suffix2, foundAt=null)
Hibernate: insert into super_hero (descriptor, found_at, name, power, prefix, suffix, id) values (?, ?, ?, ?, ?, ?, ?)
````

Camel route [SuperHeroInformationConsumptionRoute.java](https://github.com/cherepakhin/camel_boot_kafka_read/blob/main/superhero-searcher/src/main/java/com/behl/searcher/route/SuperHeroSearchScheduler.java) читает из топика superhero-information и сохраняет memory database. Прочитать сообщение через REST 

````shell
http :9090/v1/registry/superheroes
[
{
"descriptor": "Descriptor1",
"foundAt": null,
"id": "f10e37fa-0da7-4854-a292-33948f2ce330",
"name": "Name1",
"power": "Power1",
"prefix": "Prefix1",
"suffix": "Suffix1"
},
{
"descriptor": "Descriptor2",
"foundAt": null,
"id": "f10e37fa-0da7-4854-a292-33948f2ce331",
"name": "Name2",
"power": "Power2",
"prefix": "Prefix2",
"suffix": "Suffix2"
}
]
````


SuperHeroController

````shell
$ export BOOTSTRAP_SERVERS=http://192.168.1.20:9092
$ ./mvnw spring-boot:run

$ http :9090/v1/registry/superheroes
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
