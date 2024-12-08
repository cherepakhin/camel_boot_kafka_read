package ru.perm.v.camel.simple_kafka.consumer.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaConfigurationPropertiesGigaCodeTest {
    @Test
    void getTopicName() {
        KafkaConfigurationProperties properties = new KafkaConfigurationProperties();
        System.out.println(properties.getTopicName());
        //TODO: add test
        properties.topicName = "test";
        
        assertEquals( "test", properties.getTopicName());
    }
}
