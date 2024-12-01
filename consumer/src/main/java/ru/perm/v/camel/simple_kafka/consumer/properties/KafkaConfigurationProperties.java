package ru.perm.v.camel.simple_kafka.consumer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "ru.perm.v.camelkafka")
public class KafkaConfigurationProperties {

	// in application.yaml: ru.perm.v.kafka.topic-name=camel-integration
	// in application.yaml : ..."topic-name" , in code: "topicName"
	public String topicName;

}
