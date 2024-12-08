package ru.perm.v.camel.simple_kafka.consumer.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.processor.MessageDatasourceProcessor;
import ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageBodyLogger;
import ru.perm.v.camel.simple_kafka.consumer.properties.KafkaConfigurationProperties;
import ru.perm.v.camel.simple_kafka.consumer.utility.JsonDataFormatter;

import static java.lang.String.format;

@Component
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
public class ConsumerKafkaQueueCamelRoute extends RouteBuilder {

    private KafkaConfigurationProperties kafkaConfigurationProperties;

    // demo logger processor
    private MyMessageBodyLogger myMessageBodyLogger;
    private MessageDatasourceProcessor myProcessor;

//    private static final String kafkaServer = "192.168.1.20";
//    private static final String zooKeeperHost = "192.168.1.20";
//    private static final String serializerClass = "serializerClass=kafka.serializer.StringEncoder";
//    private static final String autoOffsetOption = "autoOffsetReset=smallest";
//    private static final String groupId = "testing_camel";

    public ConsumerKafkaQueueCamelRoute(
            KafkaConfigurationProperties kafkaConfigurationProperties,
            CamelContext context,
            MyMessageBodyLogger myMessageBodyLogger,
            MessageDatasourceProcessor myProcessor) {
        super(context);
        log.info("constructor with params");
        log.info(format("CamelContext: %s", context));
        log.info(format("context.isDevConsole() before set: %s", context.isDevConsole()));
        context.setDevConsole(true);
        log.info(format("context.isDevConsole() after set: %s", context.isDevConsole()));
        log.info(format("context.getComponentNames(): %s", context.getComponentNames()));
        log.info(format("myMessageBodyLogger: %s", myMessageBodyLogger));
        log.info(format("myProcessor: %s", myProcessor));
        log.info(format("kafkaConfigurationProperties: %s", kafkaConfigurationProperties));

        this.kafkaConfigurationProperties = kafkaConfigurationProperties;
        this.myMessageBodyLogger = myMessageBodyLogger;
        this.myProcessor = myProcessor;
    }

    @Override
    public void configure() throws Exception {
        log.info("kafkaConfigurationProperties.topicName {}", kafkaConfigurationProperties.topicName);
//        log.info("Read Kafka topic (from consumer): {}", kafkaConfigurationProperties.getTopicName());

//        String kafka = new StringBuilder().append(kafkaServer).append("?").append(
//                topicName).append("&").append(zooKeeperHost).append("&").append(
//                serializerClass).toString();
//        String fromKafka = new StringBuilder().append(kafka).append("&").append(
//                autoOffsetOption).append("&").append(groupId).toString();
//        String fromKafka = "kafka:192.168.1.20:9092?topic=camel-integration&groupId=testing_camel";
        String fromKafka = "kafka:camel-integration?brokers=192.168.1.20:9092";
        from(fromKafka).log("Message received from Kafka : ${body}").unmarshal(JsonDataFormatter.get(MessageDTO.class))
                .process(myMessageBodyLogger).process(myProcessor).end();
//                .process(new Processor() {
//            public void process(Exchange exchange) throws Exception {
//                if (exchange.getIn() != null) {
//                    Message message = exchange.getIn();
//                    String data = message.getBody(String.class);
//                    System.out.println("Data =" + data.toString());
//                }
//            }
//        });

//        from("kafka:" + kafkaConfigurationProperties.getTopicName()).unmarshal(JsonDataFormatter.get(MessageDTO.class))
//                .process(myMessageBodyLogger).process(myProcessor).end();
    }

    @Override
    public String toString() {
        return "MessageInformationConsumptionRoute{" +
                "topic='" + kafkaConfigurationProperties.topicName + '\'' +
                ", kafkaConfigurationProperties=" + kafkaConfigurationProperties +
                ", myMessageBodyLogger=" + myMessageBodyLogger +
                ", myProcessor=" + myProcessor +
                '}';
    }
}
