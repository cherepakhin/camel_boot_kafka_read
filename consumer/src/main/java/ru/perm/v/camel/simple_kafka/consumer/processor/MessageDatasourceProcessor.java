package ru.perm.v.camel.simple_kafka.consumer.processor;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntity;
import ru.perm.v.camel.simple_kafka.consumer.repository.MessageEntityRepository;

import java.util.logging.Logger;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class MessageDatasourceProcessor implements Processor {
    Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private final MessageEntityRepository messageEntityRepository;

    @Override
    public void process(final Exchange exchange) {
        logger.info("Process body: " + exchange.getMessage().getBody().toString());
        Object body = exchange.getMessage().getBody();
        logger.info("Body: " + body);
        try {
            MessageDTO dto = (MessageDTO) body;
            logger.info("After CAST:" + body );
            MessageEntity entity = new MessageEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            logger.info(format("Save entity: %s", entity));
            messageEntityRepository.save(entity);
        } catch (Exception e) {
            logger.severe("Error cast.");
        }
//        String json = exchange.getMessage().getBody().toString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            MessageDTO dto = objectMapper.readValue(json, MessageDTO.class);
//            MessageEntity entity = new MessageEntity();
//            entity.setId(dto.getId());
//            entity.setName(dto.getName());
//            entity.setDescription(dto.getDescription());
//            myMessageRepository.save(entity);
//        } catch (JsonProcessingException e) {
//            logger.severe("Error convert to MessageDTO json: " + json);
//        }
    }

}
