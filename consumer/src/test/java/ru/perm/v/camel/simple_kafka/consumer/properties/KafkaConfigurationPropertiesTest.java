package ru.perm.v.camel.simple_kafka.consumer.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigurationPropertiesTest {

    @Test
    void getTopicName() {
        KafkaConfigurationProperties properties = new KafkaConfigurationProperties();
        properties.setTopicName("test");

        assertEquals("test", properties.getTopicName());
    }
}