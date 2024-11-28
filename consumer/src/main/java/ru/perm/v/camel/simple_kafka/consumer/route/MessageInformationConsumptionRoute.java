package ru.perm.v.camel.simple_kafka.consumer.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageBodyLogger;
import ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageDatasourceProcessor;
import ru.perm.v.camel.simple_kafka.consumer.properties.KafkaConfigurationProperties;
import ru.perm.v.camel.simple_kafka.consumer.utility.JsonDataFormatter;

@Component
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
@RequiredArgsConstructor
public class MessageInformationConsumptionRoute extends RouteBuilder {

    private final KafkaConfigurationProperties kafkaConfigurationProperties;
    private final MyMessageBodyLogger myMessageBodyLogger;
    private final MyMessageDatasourceProcessor myProcessor;

    @Override
    public void configure() {
        log.info("Read Kafka topic: {}", kafkaConfigurationProperties.getTopicName());
        String kafkaServer = "192.168.1.20";
        String topicName = "camel-integration";
        String zooKeeperHost="192.168.1.20";
        String serializerClass = "serializerClass=kafka.serializer.StringEncoder";
        String autoOffsetOption = "autoOffsetReset=smallest";
        String groupId = "groupId=testingvinod";

        String toKafka = new StringBuilder().append(kafkaServer).append("?").append(
                topicName).append("&").append(zooKeeperHost).append("&").append(
                serializerClass).toString();
        String fromKafka = new StringBuilder().append(toKafka).append("&").append(
                autoOffsetOption).append("&").append(groupId).toString();
        from(fromKafka).process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                if (exchange.getIn() != null) {
                    Message message = exchange.getIn();
                    String data = message.getBody(String.class);
                    System.out.println("Data =" + data.toString());
                }
            }
        });

//        from("kafka:" + kafkaConfigurationProperties.getTopicName()).unmarshal(JsonDataFormatter.get(MessageDTO.class))
//                .process(myMessageBodyLogger).process(myProcessor).end();
    }

}
