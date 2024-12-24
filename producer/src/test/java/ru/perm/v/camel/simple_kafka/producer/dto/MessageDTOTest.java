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
    }

    @Test
    void testBuild() {
        MessageDTO dto = MessageDTO.build();

        assertEquals(19, dto.getName().length());
    }

    @Test
    void checkConstructorWithName() {
        MessageDTO dto = new MessageDTO("NAME");

        assertEquals("NAME", dto.getName());
    }

    @Test
    void checkConstructorUuid() {
        MessageDTO dto = new MessageDTO();

        assertNotNull(dto.getId());
    }

    @Test
    void checkDescriptionAfterConstructor() {
        MessageDTO dto = MessageDTO.build();

        assertTrue(dto.getDescription().startsWith("n:"));
    }
}