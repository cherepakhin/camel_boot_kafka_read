package ru.perm.v.camel.simple_kafka.consumer.repository;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Entity
public class MessageEntity {
    private static int counter = 0;
    @Id
    @Type(type="uuid-char")
    @Column(name = "n")
    private UUID n = UUID.randomUUID();
    @Column(name = "name")
    private String name = "";
    @Column(name = "description")
    private String description = "";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageEntity() {
    }

    public MessageEntity(UUID n, String name, String description) {
        this.n = n;
        this.name = name;
        this.description = description;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        MessageEntity.counter = counter;
    }

    public UUID getId() {
        return n;
    }

    public void setId(UUID n) {
        this.n = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity that)) return false;
        return Objects.equals(n, that.n) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, name, description);
    }
}
