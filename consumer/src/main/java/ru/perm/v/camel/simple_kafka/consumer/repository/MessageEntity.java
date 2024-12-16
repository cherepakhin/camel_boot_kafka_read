package ru.perm.v.camel.simple_kafka.consumer.repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "messages")
public class MessageEntity {
    @EqualsAndHashCode.Exclude
    public static int counter = 0;
    @Id
    @Type(type="uuid-char")
    @Column(name = "n")
    private UUID n = UUID.randomUUID();
    @Column(name = "name")
    private String name = "";
    @Column(name = "description")
    private String description = "";

    public MessageEntity() {
    }

    public MessageEntity(UUID n, String name, String description) {
        this.n = n;
        this.name = name;
        this.description = description;
    }

    public MessageEntity(String n, String name, String description) {
        this.n = UUID.fromString(n);
        this.name = name;
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

    @Override
    public String toString() {
        return "MessageEntity{" +
                "n=" + n +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
