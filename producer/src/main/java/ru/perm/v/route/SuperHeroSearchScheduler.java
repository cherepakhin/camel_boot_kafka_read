package ru.perm.v.route;

import ru.perm.v.processor.MessageBodyLoggerSecond;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ru.perm.v.dto.MessageDTO;
import ru.perm.v.processor.MessageBodyLogger;
import ru.perm.v.properties.KafkaConfigurationProperties;
import ru.perm.v.transformer.SuperHeroSearcher;
import ru.perm.v.utility.JsonDataFormatter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
public class SuperHeroSearchScheduler extends RouteBuilder {

	private final SuperHeroSearcher superHeroSearcher;
	private final MessageBodyLogger messageBodyLogger;
	private final MessageBodyLoggerSecond messageBodyLoggerSecond;
	private final KafkaConfigurationProperties kafkaConfigurationProperties;

	@Override
	public void configure() {
		final var deadLetterTopicConfiguration = kafkaConfigurationProperties.getDeadLetter();
		errorHandler(deadLetterChannel("kafka:" + deadLetterTopicConfiguration.getTopicName())
				.maximumRedeliveries(deadLetterTopicConfiguration.getRetries())
				.maximumRedeliveryDelay(deadLetterTopicConfiguration.getDelay()));

		from("timer:superhero-search-scheduler?period=5000").bean(superHeroSearcher).process(messageBodyLogger)
				.marshal(JsonDataFormatter.get(MessageDTO.class))
				.process(messageBodyLoggerSecond)
				.to("kafka:" + kafkaConfigurationProperties.getTopicName());
	}

}
