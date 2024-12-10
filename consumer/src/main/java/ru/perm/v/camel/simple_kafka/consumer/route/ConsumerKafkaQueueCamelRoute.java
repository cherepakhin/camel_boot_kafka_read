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
    private MessageDatasourceProcessor messageDatasourceProcessor;

    public ConsumerKafkaQueueCamelRoute(
            KafkaConfigurationProperties kafkaConfigurationProperties,
            CamelContext context,
            MyMessageBodyLogger myMessageBodyLogger,
            MessageDatasourceProcessor messageDatasourceProcessor) {
        super(context);
        log.info("constructor with params");
        log.info(format("CamelContext: %s", context));
        log.info(format("context.isDevConsole() before set: %s", context.isDevConsole()));
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
        from(fromKafka).log("Message received from Kafka : ${body}").unmarshal(JsonDataFormatter.get(MessageDTO.class))
                .process(myMessageBodyLogger).process(messageDatasourceProcessor).end();
    }

    public String getRoute(KafkaConfigurationProperties kafkaConfigurationProperties) {
        return  "kafka:" + kafkaConfigurationProperties.getTopicName()
                + "?brokers=" + kafkaConfigurationProperties.broker;
    }

    @Override
    public String toString() {
        return "MessageInformationConsumptionRoute{" +
                "topic='" + kafkaConfigurationProperties.topicName + '\'' +
                ", kafkaConfigurationProperties=" + kafkaConfigurationProperties +
                ", myMessageBodyLogger=" + myMessageBodyLogger +
                ", myProcessor=" + messageDatasourceProcessor +
                '}';
    }
}
