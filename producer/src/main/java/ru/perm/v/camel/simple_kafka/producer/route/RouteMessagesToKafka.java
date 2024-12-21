package ru.perm.v.camel.simple_kafka.producer.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.producer.properties.KafkaConfigurationProperties;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
public class RouteMessagesToKafka extends RouteBuilder {

    @Produce("direct:messages")
    private ProducerTemplate producerTemplate;

    private final KafkaConfigurationProperties kafkaConfigurationProperties;

    public void configure() {
        from("direct:messages").marshal().json(JsonLibrary.Jackson)
                .to("kafka:" + kafkaConfigurationProperties.getTopicName());
    }

    public void sendDto(MessageDTO dto) {
//        template.send("direct:start", dto)

//        template.sendBody("direct:start", dto);
        try {
            producerTemplate.sendBody(dto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
