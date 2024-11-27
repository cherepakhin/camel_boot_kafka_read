package ru.perm.v.camel.simple_kafka.consumer.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageRepository;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class MyMessageDatasourceProcessor implements Processor {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private final MessageRepository myMessageRepository;

    @Override
    public void process(final Exchange exchange) {
        logger.info("Process body: " + exchange.getMessage().getBody().toString());
        String json = exchange.getMessage().getBody().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MessageDTO dto = objectMapper.readValue(json, MessageDTO.class);
            MessageEntity entity = new MessageEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescriptor(dto.getDescriptor());
            myMessageRepository.save(entity);
        } catch (JsonProcessingException e) {
            logger.severe("Error convert to MessageDTO json: " + json);
        }
    }

}
