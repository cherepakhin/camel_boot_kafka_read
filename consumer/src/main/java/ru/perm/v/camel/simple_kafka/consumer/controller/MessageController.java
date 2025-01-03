package ru.perm.v.camel.simple_kafka.consumer.controller;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "The Message API")
class MessageController {

    Logger logger = Logger.getLogger(MessageController.class.getName());

    @Autowired
    private MessageEntityRepository messageEntityRepository;

    public MessageController(@Autowired MessageEntityRepository messageEntityRepository) {
        this.messageEntityRepository = messageEntityRepository;
    }

    @GetMapping("/")
    @Operation(summary = "Get all messages", description = "Retrieve a list of all messages")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public List<MessageDTO> getAllMessages() {
// Don't delete THIS comment!!!
// Variant 1 of convert
//        List<MessageDTO> messages = new ArrayList<>();
//        messageRepository
//                .findAll()
//                .forEach(messageEntity ->
//                        messages.add(
//                                new MessageDTO(
//                                        messageEntity.getId(),
//                                        messageEntity.getName(),
//                                        messageEntity.getDescription())));
//        return messages;

// Variant 2 of convert (with stream ... toList())
        Iterable<MessageEntity> iterable = messageEntityRepository.findAll();
        List<MessageEntity> entities = Lists.newArrayList(iterable);

        return entities.stream().map(e ->
                new MessageDTO(e.getN(), e.getName(), e.getDescription())).toList();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a message by ID", description = "Retrieve a message by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Message not found")
    public ResponseEntity<?> getMessageById(@Parameter(description = "Message ID") @PathVariable("id") UUID uuid) {
        logger.info(format("Get by ID=%s", uuid));
        Optional<MessageEntity> optionalMessage = messageEntityRepository.findById(uuid);
        if (optionalMessage.isPresent()) {
            MessageEntity messageEntity = optionalMessage.get();
            return ResponseEntity.ok(
                    new MessageDTO(
                            messageEntity.getN(),
                            messageEntity.getName(),
                            messageEntity.getDescription()
                    )
            );
        } else {
            return new ResponseEntity<>(format("Message with id=%s not found.", uuid), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    @Operation(summary = "Create a new message", description = "Create a new message with the provided body")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity createMessage(@Parameter(description = "Message to be created", required = true) @RequestBody MessageDTO messageDTO) {
        if (messageDTO.getId() != null) {
            Optional<MessageEntity> optionalMessage = messageEntityRepository.findById(messageDTO.getId());
            if (optionalMessage.isPresent()) {
                return new ResponseEntity<>(format("Message with id %s exist", messageDTO.getId()),
                        HttpStatus.BAD_GATEWAY);
            }
        }

        MessageEntity messageEntity = new MessageEntity(UUID.randomUUID(), messageDTO.getName(), messageDTO.getDescription());
        MessageEntity savedEntity = messageEntityRepository.save(messageEntity);
        MessageDTO dto = new MessageDTO(savedEntity.getN(), savedEntity.getName(), savedEntity.getDescription());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a message", description = "Delete a message by its unique identifier")
    @ApiResponse(responseCode = "204", description = "Message deleted successfully")
    @ApiResponse(responseCode = "404", description = "Message not found")
    public ResponseEntity deleteMessage(@Parameter(description = "Message ID") @PathVariable("id") UUID uuid) {
        if (uuid == null) {
            return new ResponseEntity<>("UUID is required", HttpStatus.BAD_REQUEST);
        }
        logger.info(format("Delete by ID=%s", uuid));
        if (!messageEntityRepository.existsById(uuid)) {
            return new ResponseEntity<>(format("Message with id=%s not found.", uuid), HttpStatus.NOT_FOUND);
        }
        messageEntityRepository.deleteById(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete all messages")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public void deleteAll() {
        logger.info("Delete all messages");
        messageEntityRepository.deleteMessages();
    }

    //TODO: validate
    @PostMapping("/")
    @Operation(summary = "Update a message", description = "Update a message")
    @ApiResponse(responseCode = "204", description = "Message updated successfully")
    @ApiResponse(responseCode = "404", description = "Message not found")
    public ResponseEntity updateMessage(@Parameter(description = "DTO Message") @RequestBody MessageDTO messageDTO) {
        logger.info(format("Update message %s", messageDTO));
        if (messageDTO.getId() == null) {
            return new ResponseEntity<>("UUID is required", HttpStatus.BAD_REQUEST);
        }
        if (!messageEntityRepository.existsById(messageDTO.getId())) {
            return new ResponseEntity<>(format("Message with id=%s not found.", messageDTO.getId()), HttpStatus.NOT_FOUND);
        }
        MessageEntity messageEntity = new MessageEntity(messageDTO.getId(), messageDTO.getName(), messageDTO.getDescription());
        MessageEntity result = messageEntityRepository.save(messageEntity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
