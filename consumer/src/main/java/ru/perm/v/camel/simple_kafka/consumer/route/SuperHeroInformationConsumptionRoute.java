package ru.perm.v.camel.simple_kafka.consumer.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ru.perm.v.camel.simple_kafka.consumer.entity.SuperHero;
import ru.perm.v.camel.simple_kafka.consumer.processor.MessageBodyLogger;
import ru.perm.v.camel.simple_kafka.consumer.processor.SuperHeroRegistryDatasourceProcessor;
import ru.perm.v.camel.simple_kafka.consumer.properties.KafkaConfigurationProperties;
import ru.perm.v.camel.simple_kafka.consumer.utility.JsonDataFormatter;

import lombok.RequiredArgsConstructor;

@Component
@EnableConfigurationProperties(value = KafkaConfigurationProperties.class)
@RequiredArgsConstructor
public class SuperHeroInformationConsumptionRoute extends RouteBuilder {

	private final KafkaConfigurationProperties kafkaConfigurationProperties;
	private final MessageBodyLogger messageBodyLogger;
	private final SuperHeroRegistryDatasourceProcessor superHeroRegistryDatasourceProcessor;

	@Override
	public void configure() {
		from("kafka:" + kafkaConfigurationProperties.getTopicName()).unmarshal(JsonDataFormatter.get(SuperHero.class))
				.process(messageBodyLogger).process(superHeroRegistryDatasourceProcessor).end();
	}

}
