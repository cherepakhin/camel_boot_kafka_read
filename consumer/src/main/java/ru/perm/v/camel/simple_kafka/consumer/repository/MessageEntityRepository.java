package ru.perm.v.camel.simple_kafka.consumer.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@Transactional
public interface MessageEntityRepository extends CrudRepository<MessageEntity, UUID> {
    @Modifying
    @Query("delete from MessageEntity")
    void deleteMessages();
}
