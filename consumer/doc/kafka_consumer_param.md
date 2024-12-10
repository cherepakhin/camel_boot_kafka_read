Consumer имеет следующие параметры:

ConsumerConfig values:
allow.auto.create.topics = true
auto.commit.interval.ms = 5000
auto.offset.reset = latest
bootstrap.servers = [192.168.1.20:9092]
check.crcs = true
client.dns.lookup = use_all_dns_ips
client.id = consumer-c7ac056e-97a6-4432-b937-73f1500600a4-1
client.rack =
connections.max.idle.ms = 540000
default.api.timeout.ms = 60000
enable.auto.commit = true
exclude.internal.topics = true
fetch.max.bytes = 52428800
fetch.max.wait.ms = 500
fetch.min.bytes = 1
group.id = c7ac056e-97a6-4432-b937-73f1500600a4
group.instance.id = null
heartbeat.interval.ms = 3000
interceptor.classes = []
internal.leave.group.on.close = true
internal.throw.on.fetch.stable.offset.unsupported = false
isolation.level = read_uncommitted
key.deserializer = class org.apache.kafka.common.serialization.StringDeserializer
max.partition.fetch.bytes = 1048576
----------------------
max.poll.interval.ms = 300000 // по умолчанию 5 минут, означает максимальную задержку между вызовами
    poll() при использовании потребителей. Если метод poll() не вызывается до
    истечения этого тайм-аута, то потребитель считается отказавшим, и группа выполняет перебалансировку,
    чтобы переназначить разделы другому члену группы потребителей. (https://bigdataschool.ru/blog/slow-consumers-in-kafka-and-default-configurations.html)

max.poll.records = 500 // Означает максимальное количество записей, возвращаемых одним вызовом poll() (https://bigdataschool.ru/blog/slow-consumers-in-kafka-and-default-configurations.html)

Эти два свойства конфигурации определяют требования к приложению-потребителю: оно должно иметь возможность потреблять max.poll.records записей за max.poll.interval.ms миллисекунд.
В данном случае 500 сообщений должны обработаться за 300000 мс (5 мин.)
----------------------
metadata.max.age.ms = 300000
metric.reporters = []
metrics.num.samples = 2
metrics.recording.level = INFO
metrics.sample.window.ms = 30000
partition.assignment.strategy = [org.apache.kafka.clients.consumer.RangeAssignor]
receive.buffer.bytes = 65536
reconnect.backoff.max.ms = 1000
reconnect.backoff.ms = 50
request.timeout.ms = 40000
security.protocol = PLAINTEXT
security.providers = null
send.buffer.bytes = 131072
session.timeout.ms = 10000
socket.connection.setup.timeout.max.ms = 30000
socket.connection.setup.timeout.ms = 10000
value.deserializer = class org.apache.kafka.common.serialization.StringDeserializer
