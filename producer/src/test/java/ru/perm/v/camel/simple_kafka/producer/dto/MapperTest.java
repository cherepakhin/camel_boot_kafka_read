package ru.perm.v.camel.simple_kafka.producer.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MapperTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void toJson() {
        UUID uuid = new UUID(8,2);
        MessageDTO dto = new MessageDTO();
        dto.setId(uuid);
        dto.setName("NAME");
        dto.setDescription("DESCRIPTION");
        try {
            assertEquals("{\"id\":\"00000000-0000-0008-0000-000000000002\",\"name\":\"NAME\",\"description\":\"DESCRIPTION\"}",
                    mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    void fromJson() {
        String json = "{\"id\":\"00000000-0000-0008-0000-000000000002\",\"name\":\"NAME\",\"description\":\"DESCRIPTION\"}";

        try {
            MessageDTO messageDtoFromJson = mapper.readValue(json, MessageDTO.class);
            UUID uuid = new UUID(8,2);

            MessageDTO dto = new MessageDTO();
            dto.setId(uuid);
            dto.setName("NAME");
            dto.setDescription("DESCRIPTION");

            assertEquals(dto, messageDtoFromJson);
        } catch (JsonProcessingException e) {
            fail();
        }
    }
}
