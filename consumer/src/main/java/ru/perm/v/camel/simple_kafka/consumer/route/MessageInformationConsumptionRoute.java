package ru.perm.v.camel.simple_kafka.consumer.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageBodyLogger;
import ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageDatasourceProcessor;
import ru.perm.v.camel.simple_kafka.consumer.properties.KafkaConfigurationProperties;
import ru.perm.v.camel.simple_kafka.consumer.utility.JsonDataFormatter;

import static java.lang.String.format;

@Component
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
public class MessageInformationConsumptionRoute extends RouteBuilder {

    private String topic = "camel-integration";
    @Autowired
    private KafkaConfigurationProperties kafkaConfigurationProperties;
    @Autowired
    private MyMessageBodyLogger myMessageBodyLogger;
    @Autowired
    private MyMessageDatasourceProcessor myProcessor;

    String kafkaServer = "192.168.1.20";
    String topicName = "camel-integration";
    String zooKeeperHost="192.168.1.20";
    String serializerClass = "serializerClass=kafka.serializer.StringEncoder";
    String autoOffsetOption = "autoOffsetReset=smallest";
    String groupId = "testing_camel";

//    public MessageInformationConsumptionRoute() {
//        log.info("default constructor");
//    }

    public MessageInformationConsumptionRoute(CamelContext context,
                                              MyMessageBodyLogger myMessageBodyLogger,
                                              MyMessageDatasourceProcessor myProcessor) {
        super(context);
        log.info("constructor with params");
        log.info(format("CamelContext: %s", context));
        log.info(format("context.isDevConsole() before set: %s", context.isDevConsole()));
        context.setDevConsole(true);
        log.info(format("context.isDevConsole() after set: %s", context.isDevConsole()));
        log.info(format("context.getComponentNames(): %s", context.getComponentNames()));
        log.info(format("myMessageBodyLogger: %s", myMessageBodyLogger));
        log.info(format("myProcessor: %s", myProcessor));

//        this.kafkaConfigurationProperties = kafkaConfigurationProperties;
        this.topic = "camel-integration";
        this.myMessageBodyLogger = myMessageBodyLogger;
        this.myProcessor = myProcessor;
    }

    @Override
    public void configure() throws Exception {
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
                "topic='" + topic + '\'' +
                ", kafkaConfigurationProperties=" + kafkaConfigurationProperties +
                ", myMessageBodyLogger=" + myMessageBodyLogger +
                ", myProcessor=" + myProcessor +
                '}';
    }
}
