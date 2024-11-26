package ru.perm.v.camel.simple_kafka.producer.route;

import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.producer.processor.MessageBodyLogger;
import ru.perm.v.camel.simple_kafka.producer.processor.MessageBodyLoggerSecond;
import ru.perm.v.camel.simple_kafka.producer.properties.KafkaConfigurationProperties;
import ru.perm.v.camel.simple_kafka.producer.transformer.MessageSearcher;
import ru.perm.v.camel.simple_kafka.producer.utility.JsonDataFormatter;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
public class MessageSearchScheduler extends RouteBuilder {

	private final MessageSearcher superHeroSearcher;
	private final MessageBodyLogger messageBodyLogger;
	private final MessageBodyLoggerSecond messageBodyLoggerSecond;
	private final KafkaConfigurationProperties kafkaConfigurationProperties;

	@Override
	public void configure() {
		final var deadLetterTopicConfiguration = kafkaConfigurationProperties.getDeadLetter();
		errorHandler(deadLetterChannel("kafka:" + deadLetterTopicConfiguration.getTopicName())
				.maximumRedeliveries(deadLetterTopicConfiguration.getRetries())
				.maximumRedeliveryDelay(deadLetterTopicConfiguration.getDelay()));

		from("timer:v-search-scheduler?period=5000").bean(superHeroSearcher).process(messageBodyLogger)
				.marshal(JsonDataFormatter.get(MessageDTO.class))
				.process(messageBodyLoggerSecond)
				.to("kafka:" + kafkaConfigurationProperties.getTopicName());
	}

}