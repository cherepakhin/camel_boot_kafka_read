package ru.perm.v.camel.simple_kafka.consumer.repository;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageEntityTest {

    @Test
    void messageEntityConstructor() {
        MessageEntity messageEntity = new MessageEntity("9501f96f-29ba-40e4-9b08-d3db418d96dc", "NAME2", "DESCRIPTION2");

        assertEquals(UUID.fromString("9501f96f-29ba-40e4-9b08-d3db418d96dc"), messageEntity.getId());
        assertEquals("NAME2", messageEntity.getName());
        assertEquals("DESCRIPTION2", messageEntity.getDescription());
    }

    @Test
    void equalsTest() {
        MessageEntity messageEntity1 = new MessageEntity("9501f96f-29ba-40e4-9b08-d3db418d96dc", "NAME2", "DESCRIPTION2");
        MessageEntity messageEntity2 = new MessageEntity("9501f96f-29ba-40e4-9b08-d3db418d96dc", "NAME2", "DESCRIPTION2");

        assertEquals(messageEntity1, messageEntity2);
    }
}