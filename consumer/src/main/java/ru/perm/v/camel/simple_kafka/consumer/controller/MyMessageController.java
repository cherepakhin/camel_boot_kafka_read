package ru.perm.v.camel.simple_kafka.consumer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "The Message API")
class MessageController {

    private MessageRepository messageRepository;

    public MessageController(@Autowired MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
	@Operation(summary = "Get all messages", description = "Retrieve a list of all messages")
	@ApiResponse(responseCode = "200", description = "Successful operation")
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
				.map(m -> new MessageDTO(m.getId(), m.getName(), m.getDescriptor()))
				.collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a message by ID", description = "Retrieve a message by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Message not found")
    public ResponseEntity<MessageDTO> getMessageById(@Parameter(description = "Message ID") @PathVariable UUID uuid) {
        Optional<MessageEntity> optionalMessage = messageRepository.findById(uuid);
        if (optionalMessage.isPresent()) {
            return ResponseEntity.ok(new MessageDTO(optionalMessage.get().getId(), optionalMessage.get().getName(), optionalMessage.get().getDescriptor()));
        } else {
            return ResponseEntity.notFound().build();
        }
//		return messageRepository.findById(uuid).map(m -> ResponseEntity.ok(MessageDTO(m.id, m.body)))
//				.map { ResponseEntity.ok(MessageDTO(it.id, it.body)) }
//            .orElse(ResponseEntity.notFound().build())
    }

//	@PutMapping()
    //	@Operation(summary = "Create a new message", description = "Create a new message with the provided body")
//    @ApiResponse(responseCode = "200", description = "Successful operation", content =[Content(schema = Schema(implementation = MessageDTO::class))])

    @ApiResponse(responseCode = "400", description = "Invalid input")
    public MessageDTO createMessage(@Parameter(description = "Message to be created", required = true) @RequestBody MessageDTO messageDTO) {
        MessageEntity messageEntity = new MessageEntity(messageDTO.getId(), messageDTO.getName(), messageDTO.getDescriptor());
        MessageEntity savedEntity = messageRepository.save(messageEntity);
        return new MessageDTO(savedEntity.getId(), savedEntity.getName(), savedEntity.getDescriptor());
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
