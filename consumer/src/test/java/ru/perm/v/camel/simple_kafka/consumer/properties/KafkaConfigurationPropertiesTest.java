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

    @Test
    void testToString() {
        KafkaConfigurationProperties properties = new KafkaConfigurationProperties();
        properties.topicName ="TEST_TOPIC";
        properties.broker="192.168.1.20";

        assertEquals("KafkaConfigurationProperties{topicName='TEST_TOPIC', broker='192.168.1.20'}", properties.toString());
    }
}