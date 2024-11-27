package ru.perm.v.camel.simple_kafka.consumer.controller;


import org.junit.jupiter.api.Test;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageControllerTest {

    @Test
    void getAllMessages() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        String validUUID1 = "26929514-237c-11ed-861d-0242ac120001";
        String validUUID2 = "26929514-237c-11ed-861d-0242ac120002";
        MessageEntity message1 = new MessageEntity(UUID.fromString(validUUID1), "NAME1", "DESCR1");
        MessageEntity message2 = new MessageEntity(UUID.fromString(validUUID2), "NAME2", "DESCR2");

        when(messageRepository.findAll()).thenReturn(List.of(message1, message2));
        MessageController ctrl = new MessageController(messageRepository);
        List<MessageDTO> messages = ctrl.getAllMessages();

        assertEquals(2, messages.size());
        assertEquals(new MessageDTO(UUID.fromString(validUUID1), "NAME1", "DESCR1"), messages.get(0));
        assertEquals(new MessageDTO(UUID.fromString(validUUID2), "NAME2", "DESCR2"), messages.get(1));
    }
    @Test
    void getAllMessagesForEmpty() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        when(messageRepository.findAll()).thenReturn(List.of());

        MessageController ctrl = new MessageController(messageRepository);
        List<MessageDTO> messages = ctrl.getAllMessages();

        assertEquals(0, messages.size());
    }
}