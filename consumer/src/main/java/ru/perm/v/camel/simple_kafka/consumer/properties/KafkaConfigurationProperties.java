package ru.perm.v.camel.simple_kafka.consumer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "ru.perm.v.kafka")
public class KafkaConfigurationProperties {

	// in application.properties: ru.perm.v.kafka.topic-name=camel-integration
	// in application.properties : ..."topic-name" , in code: "topicName"
	private String topicName;

}
