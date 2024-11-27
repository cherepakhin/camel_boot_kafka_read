package ru.perm.v.camel.simple_kafka.consumer.route;

import lombok.RequiredArgsConstructor;
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
        from("kafka:" + kafkaConfigurationProperties.getTopicName()).unmarshal(JsonDataFormatter.get(MessageDTO.class))
                .process(myMessageBodyLogger).process(myProcessor).end();
    }

}
