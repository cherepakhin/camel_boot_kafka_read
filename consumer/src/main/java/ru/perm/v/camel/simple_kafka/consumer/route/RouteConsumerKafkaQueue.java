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
public class RouteConsumerKafkaQueue extends RouteBuilder {

    private final KafkaConfigurationProperties kafkaConfigurationProperties;

    // demo logger processor
    private final MyMessageBodyLogger myMessageBodyLogger;
    private final MessageDatasourceProcessor messageDatasourceProcessor;

    public RouteConsumerKafkaQueue(
            KafkaConfigurationProperties kafkaConfigurationProperties,
            CamelContext context,
            MyMessageBodyLogger myMessageBodyLogger,
            MessageDatasourceProcessor messageDatasourceProcessor) {
        super(context);
        log.info("constructor with params");
        log.info(format("CamelContext: %s", context)); // CamelContext: SpringCamelContext(camel-1) with spring id camel_integration_consumer
        log.info(format("context.isDevConsole() before set: %s", context.isDevConsole())); // context.isDevConsole() before set: false
        context.setDevConsole(true);
        log.info(format("context.isDevConsole() after set: %s", context.isDevConsole()));
        log.info(format("context.getComponentNames(): %s", context.getComponentNames()));
        log.info(format("myMessageBodyLogger: %s", myMessageBodyLogger));
        log.info(format("myProcessor: %s", messageDatasourceProcessor));
        log.info(format("kafkaConfigurationProperties: %s", kafkaConfigurationProperties));

        this.kafkaConfigurationProperties = kafkaConfigurationProperties;
        this.myMessageBodyLogger = myMessageBodyLogger;
        this.messageDatasourceProcessor = messageDatasourceProcessor;
    }

    @Override
    public void configure() throws Exception {
        log.info("kafkaConfigurationProperties.topicName: {}", kafkaConfigurationProperties.topicName);
        log.info("kafkaConfigurationProperties.broker: {}", kafkaConfigurationProperties.broker);
        String fromKafka = getRoute(kafkaConfigurationProperties);
        log.info("fromKafka: {}", fromKafka);
        from(fromKafka)
                .log("Message received from Kafka : ${body}")
                .unmarshal(JsonDataFormatter.get(MessageDTO.class))
                .process(myMessageBodyLogger)
                .process(messageDatasourceProcessor)
                .end();
    }

    public String getRoute(KafkaConfigurationProperties kafkaConfigurationProperties) {
        return  "kafka:" + kafkaConfigurationProperties.getTopicName()
                + "?brokers=" + kafkaConfigurationProperties.broker + "&groupId=vasiGroup&pollTimeoutMs=1000&maxPollRecords=10&autoOffsetReset=earliest";
    }

    @Override
    public String toString() {
        return "RouteConsumerKafkaQueue{" +
                "topic='" + kafkaConfigurationProperties.topicName + '\'' +
                ", kafkaConfigurationProperties=" + kafkaConfigurationProperties +
                ", myMessageBodyLogger=" + myMessageBodyLogger +
                ", myProcessor=" + messageDatasourceProcessor +
                '}';
    }
}
