package ru.perm.v.camel.simple_kafka.producer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class MessageDTO {
    private static int counter = 0;
    private UUID id = UUID.randomUUID();
    private String name = "";
    private String description = "";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageDTO() {
    }

    public MessageDTO(String name) {
        this();
        this.name = name;
    }

    public static MessageDTO build() {
        MessageDTO dto = new MessageDTO();
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);
        dto.setName(formattedDateTime);
        dto.description = "n: " + counter++;
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageDTO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
