package ru.perm.v.camel.simple_kafka.producer.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageDTOTest {
    @Test
    void build() {
        MessageDTO message = MessageDTO.build();

        assertNotEquals("", message.getName());
    }
    @Test
    void toStringTest() {
        MessageDTO message = MessageDTO.build();

        assertTrue(message.toString().contains("MessageDTO{"));
        assertTrue(message.toString().contains("name="));
        assertTrue(message.toString().contains("descriptor="));
    }
}