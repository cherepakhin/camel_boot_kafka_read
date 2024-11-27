package ru.perm.v.camel.simple_kafka.consumer.controller;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Test
    void getMessageByIdExist() {
        MessageRepository messageRepository = mock(MessageRepository.class);
        UUID uuid = new UUID(2, 2);
        MessageEntity messageEntity = new MessageEntity(uuid, "NAME", "DESCRIPTION");
        when(messageRepository.findById(uuid)).thenReturn(Optional.of(messageEntity));
        MessageController ctrl = new MessageController(messageRepository);

        ResponseEntity<MessageDTO> receivedMessage = ctrl.getMessageById(uuid);

        assertEquals(uuid, Objects.requireNonNull(receivedMessage.getBody()).getId());
        assertEquals("NAME", receivedMessage.getBody().getName());
        assertEquals("DESCRIPTION", receivedMessage.getBody().getDescription());
    }

    @Test
    void getMessageByIdNotExist() {
        UUID uuid = new UUID(2, 2);
        MessageRepository messageRepository = mock(MessageRepository.class);
        when(messageRepository.findById(uuid)).thenReturn(Optional.empty());
        MessageController ctrl = new MessageController(messageRepository);

        ResponseEntity<MessageDTO> receivedMessage = ctrl.getMessageById(uuid);

        assertNull(receivedMessage.getBody());
        assertEquals(HttpStatus.NOT_FOUND, receivedMessage.getStatusCode());
    }
}