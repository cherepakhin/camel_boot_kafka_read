package ru.perm.v.camel.simple_kafka.producer.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.producer.processor.MessageBodyLogger;
import ru.perm.v.camel.simple_kafka.producer.processor.MessageBodyLoggerSecond;
import ru.perm.v.camel.simple_kafka.producer.properties.KafkaConfigurationProperties;
import ru.perm.v.camel.simple_kafka.producer.transformer.MessageBuilder;
import ru.perm.v.camel.simple_kafka.producer.utility.JsonDataFormatter;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
public class RouteMessageScheduler extends RouteBuilder {

    private final MessageBuilder messageBuilder;
    private final MessageBodyLogger messageBodyLogger;
    private final MessageBodyLoggerSecond messageBodyLoggerSecond;
    private final KafkaConfigurationProperties kafkaConfigurationProperties;

    /**
     * period=2000 - каждые 2 сек. messageBuilder генерирует сообщение
     * period=0 - sheduler отключен
     * сообщение логируется messageBodyLogger
     * сообщение логируется вторым логером messageBodyLoggerSecond (для интереса)
     * JsonDataFormatter спец.средство Camel для перевода объекта в json (типа ObjectMapper).
     * JsonDataFormatter принимает поток сообщений и конвертирует их в JSON.
     */
    @Override
    public void configure() {
        from("timer:v-producer-scheduler?period=2000").bean(messageBuilder).process(messageBodyLogger)
                .marshal(JsonDataFormatter.get(MessageDTO.class))
                .process(messageBodyLoggerSecond)
                .to("kafka:" + kafkaConfigurationProperties.getTopicName());
    }
}
