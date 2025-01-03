package ru.perm.v.camel.simple_kafka.producer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Промежуточный класс для импортирования параметров ru.perm.v.kafka.* из application.yaml
 */
@Data
@ConfigurationProperties(prefix = "ru.perm.v.kafka")
public class KafkaConfigurationProperties {

	private String topicName;
	private Integer shedulePeriod;

}
