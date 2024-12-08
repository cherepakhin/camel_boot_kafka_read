Генерирует сообщения и отправляет сообщения в Kafka в очередь "camel-integration" 
(см. application.properties "ru.perm.v.kafka.topic-name=camel-integration")

сообщения читает другой сервис ~/prog/java/camel/camel-integration-spring-boot-kafka/consumer

Для проверки работы запустить consumer

````shell
cd consumer
./run.sh
````

Запустить producer:

````shell
cd producer
./run.sh
````

Producer отправляет сообщения в Kafka каждые 5 сек. Consumer их принимает и сохраняет в базу данных. Проверить сохраненные сообщения запросом:

````shell
http :9090/api/messages/

[
    {
        "description": "n: 1",
        "id": "478bd6ea-0fdb-4aed-be5b-55c9bb14e7ad",
        "name": "2024-12-08 10:49:51"
    },
    {
        "description": "n: 2",
        "id": "8213b6c8-c768-4be4-8395-ae1e6f97e797",
        "name": "2024-12-08 10:49:56"
    },
    ....
````

Сообщения отправляются каждые 5 сек, время отправки в __name__.