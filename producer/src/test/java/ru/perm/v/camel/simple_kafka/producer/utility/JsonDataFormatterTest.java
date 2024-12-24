package ru.perm.v.camel.simple_kafka.producer.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonDataFormatterTest {
    @Test
    public void format() throws JsonProcessingException {
        UUID uuid = UUID.fromString("00000000-0000-0008-0000-000000000002");

        MessageDTO dto = new MessageDTO();
        dto.setId(uuid);
        dto.setName("NAME");
        dto.setDescription("DESCRIPTION");

        String json = new ObjectMapper().writeValueAsString(dto);
        MessageDTO objFromJson = new ObjectMapper().readValue(json, MessageDTO.class);

        assertEquals(dto, objFromJson);
    }

}