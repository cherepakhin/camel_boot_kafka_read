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
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "The Message API")
class MessageController {

    Logger logger = Logger.getLogger(MessageController.class.getName());

    @Autowired
    private MessageRepository messageRepository;

    public MessageController(@Autowired MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    @Operation(summary = "Get all messages", description = "Retrieve a list of all messages")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public List<MessageDTO> getAllMessages() {
        Iterable<MessageEntity> iterable = messageRepository.findAll();
        List<MessageEntity> entities = Lists.newArrayList(iterable);
        return entities.stream().map(messageEntity ->
                new MessageDTO(messageEntity.getId(), messageEntity.getName(), messageEntity.getDescription()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a message by ID", description = "Retrieve a message by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Message not found")
    public ResponseEntity<?> getMessageById(@Parameter(description = "Message ID") @PathVariable("id") UUID uuid) {
        logger.info(format("Get by ID=%s", uuid));
        Optional<MessageEntity> optionalMessage = messageRepository.findById(uuid);
        if (optionalMessage.isPresent()) {
            MessageEntity messageEntity = optionalMessage.get();
            return ResponseEntity.ok(
                    new MessageDTO(
                            messageEntity.getId(),
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
            Optional<MessageEntity> optionalMessage = messageRepository.findById(messageDTO.getId());
            if (optionalMessage.isPresent()) {
                return new ResponseEntity<>(format("Message with id %s exist", messageDTO.getId()),
                        HttpStatus.BAD_GATEWAY);
            }
        }

        MessageEntity messageEntity = new MessageEntity(UUID.randomUUID(), messageDTO.getName(), messageDTO.getDescription());
        MessageEntity savedEntity = messageRepository.save(messageEntity);
        MessageDTO dto = new MessageDTO(savedEntity.getId(), savedEntity.getName(), savedEntity.getDescription());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete all messages")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public void deleteAll() {
        logger.info("Delete all messages");
        messageRepository.deleteMessages();
    }
//	@PutMapping("/{id}")
//	@Operation(summary = "Update an existing message", description = "Update an existing message with the provided body")
//	@ApiResponse(responseCode = "200", description = "Successful operation", content = [Content(schema = Schema(implementation = MessageDTO::class))])
//	@ApiResponse(responseCode = "400", description = "Invalid input")
//	@ApiResponse(responseCode = "404", description = "Message not found")
//	fun updateMessage(
//			@Parameter(description = "Message ID") @PathVariable id: UUID,
//			@Parameter(description = "Updated message details", required = true) @RequestBody messageDTO: MessageDTO
//    ): ResponseEntity<MessageDTO> {
//		return messageRepository.findById(id)
//				.map {
//			it.body = messageDTO.body
//			ResponseEntity.ok(MessageDTO(messageRepository.save(it).id, it.body))
//		}
//            .orElse(ResponseEntity.notFound().build())
//	}
//
//	@DeleteMapping("/{id}")
//	@Operation(summary = "Delete a message", description = "Delete a message by its unique identifier")
//	@ApiResponse(responseCode = "204", description = "Message deleted successfully")
//	@ApiResponse(responseCode = "404", description = "Message not found")
//	fun deleteMessage(@Parameter(description = "Message ID") @PathVariable id: UUID): ResponseEntity<Void> {
//		return if (messageRepository.existsById(id)) {
//			messageRepository.deleteById(id)
//			ResponseEntity.noContent().build()
//		} else {
//			ResponseEntity.notFound().build()
//		}
//	}
}
