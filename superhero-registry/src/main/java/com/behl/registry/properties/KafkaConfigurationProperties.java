package com.behl.registry.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.kafka")
public class KafkaConfigurationProperties {

	// in application.properties: com.behl.kafka.topic-name=superhero-information
	// in application.properties : ..."topic-name" , in code: "topicName"
	private String topicName;

}
