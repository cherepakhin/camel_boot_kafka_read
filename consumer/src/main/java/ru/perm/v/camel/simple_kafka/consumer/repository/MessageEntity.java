package ru.perm.v.camel.simple_kafka.consumer.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Entity
public class MessageEntity {
    private static int counter = 0;
    @Id
    private UUID id = UUID.randomUUID();
    @Column(name = "name")
    private String name = "";
    @Column(name = "descriptor")
    private String descriptor = "";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageEntity() {
    }

    public MessageEntity(UUID id, String name, String descriptor) {
        this.id = id;
        this.name = name;
        this.descriptor = descriptor;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        MessageEntity.counter = counter;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, descriptor);
    }
}
