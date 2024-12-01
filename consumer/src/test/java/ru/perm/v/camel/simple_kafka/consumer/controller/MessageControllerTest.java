package ru.perm.v.camel.simple_kafka.consumer.controller;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntityRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Test
    void getAllMessages() {
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        String validUUID1 = "26929514-237c-11ed-861d-0242ac120001";
        String validUUID2 = "26929514-237c-11ed-861d-0242ac120002";
        MessageEntity message1 = new MessageEntity(UUID.fromString(validUUID1), "NAME1", "DESCR1");
        MessageEntity message2 = new MessageEntity(UUID.fromString(validUUID2), "NAME2", "DESCR2");

        when(messageEntityRepository.findAll()).thenReturn(List.of(message1, message2));
        MessageController ctrl = new MessageController(messageEntityRepository);
        List<MessageDTO> messages = ctrl.getAllMessages();

        assertEquals(2, messages.size());
        assertEquals(new MessageDTO(UUID.fromString(validUUID1), "NAME1", "DESCR1"), messages.get(0));
        assertEquals(new MessageDTO(UUID.fromString(validUUID2), "NAME2", "DESCR2"), messages.get(1));
    }

    @Test
    void getAllMessagesForEmpty() {
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.findAll()).thenReturn(List.of());

        MessageController ctrl = new MessageController(messageEntityRepository);
        List<MessageDTO> messages = ctrl.getAllMessages();

        assertEquals(0, messages.size());
    }

    @Test
    void getMessageByIdExist() {
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        UUID uuid = new UUID(2, 2);
        MessageEntity messageEntity = new MessageEntity(uuid, "NAME", "DESCRIPTION");
        when(messageEntityRepository.findById(uuid)).thenReturn(Optional.of(messageEntity));
        MessageController ctrl = new MessageController(messageEntityRepository);

        ResponseEntity<MessageDTO> receivedMessage = (ResponseEntity<MessageDTO>) ctrl.getMessageById(uuid);

        assertEquals(uuid, Objects.requireNonNull(receivedMessage.getBody()).getId());
        assertEquals("NAME", receivedMessage.getBody().getName());
        assertEquals("DESCRIPTION", receivedMessage.getBody().getDescription());
    }

    @Test
    void getMessageByIdNotExist() {
        UUID uuid = new UUID(2, 2);
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.findById(uuid)).thenReturn(Optional.empty());
        MessageController ctrl = new MessageController(messageEntityRepository);

        ResponseEntity<?> receivedMessage = ctrl.getMessageById(uuid);

        assertEquals("Message with id=00000000-0000-0002-0000-000000000002 not found.",
                receivedMessage.getBody());
        assertEquals(HttpStatus.NOT_FOUND, receivedMessage.getStatusCode());
    }

    @Test
    void createMessage() {
        UUID uuid = new UUID(2, 2);
        String NAME = "NAME";
        String DESCRIPTION = "DESCRIPTION";
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);

        MessageEntity createdMessageEntity = new MessageEntity(uuid, NAME, DESCRIPTION);
        when(messageEntityRepository.save(any())).thenReturn(createdMessageEntity);
        MessageController ctrl = new MessageController(messageEntityRepository);

        ResponseEntity<?> createdMessageDTO = ctrl.createMessage(new MessageDTO(uuid, NAME, DESCRIPTION));
        MessageDTO body = (MessageDTO) createdMessageDTO.getBody();

        assertEquals(uuid, body.getId());
        assertEquals(NAME, body.getName());
        assertEquals(DESCRIPTION, body.getDescription());
    }
    @Test
    void createMessageIfExist() {
        UUID uuid = new UUID(2, 2);
        String NAME = "NAME";
        String DESCRIPTION = "DESCRIPTION";
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.findById(uuid)).thenReturn(Optional.of(new MessageEntity()));
        MessageEntity createdMessageEntity = new MessageEntity(uuid, NAME, DESCRIPTION);
        when(messageEntityRepository.save(any())).thenReturn(createdMessageEntity);
        MessageController ctrl = new MessageController(messageEntityRepository);

        ResponseEntity<?> result = ctrl.createMessage(new MessageDTO(uuid, NAME, DESCRIPTION));

        assertEquals(502, result.getStatusCode().value());
        assertEquals("Message with id 00000000-0000-0002-0000-000000000002 exist", result.getBody().toString());
    }

    @Test
    void deleteMessageIfExist() {
        UUID uuid = new UUID(2, 2);
        MessageEntity messageEntity = new MessageEntity(uuid, "NAME", "DESCRIPTION");
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.existsById(uuid)).thenReturn(true);
        when(messageEntityRepository.findById(uuid)).thenReturn(Optional.of(messageEntity));
        MessageController ctrl = new MessageController(messageEntityRepository);

        var result = ctrl.deleteMessage(uuid);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteMessageIfNotExist() {
        UUID uuid = new UUID(2, 2);
        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.existsById(uuid)).thenReturn(false);
        MessageController ctrl = new MessageController(messageEntityRepository);

        var result = ctrl.deleteMessage(uuid);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Message with id=00000000-0000-0002-0000-000000000002 not found.", result.getBody().toString());
    }

    @Test
    void updateMessageForNullUUID() {
        MessageDTO dto = new MessageDTO(null, "NAME", "DESCRIPTION");
        MessageController ctrl = new MessageController(null);
        ResponseEntity<?> result = ctrl.updateMessage(dto);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("UUID is required", result.getBody().toString());
    }

    @Test
    void updateMessageForNotExist() {
        UUID uuid = new UUID(2, 2);

        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.existsById(uuid)).thenReturn(false);
        MessageController ctrl = new MessageController(messageEntityRepository);

        ResponseEntity<?> result = ctrl.updateMessage(new MessageDTO(uuid, "NAME", "DESCRIPTION"));

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Message with id=00000000-0000-0002-0000-000000000002 not found.", result.getBody().toString());
    }

    @Test
    void updateMessage() {
        UUID uuid = new UUID(2, 2);

        MessageEntityRepository messageEntityRepository = mock(MessageEntityRepository.class);
        when(messageEntityRepository.existsById(uuid)).thenReturn(true);
        MessageController ctrl = new MessageController(messageEntityRepository);

        ResponseEntity<?> result = ctrl.updateMessage(new MessageDTO(uuid, "NAME", "DESCRIPTION"));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(messageEntityRepository, times(1)).save(any(MessageEntity.class));
    }
}