package ru.perm.v.camel.simple_kafka.consumer.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.perm.v.camel.simple_kafka.consumer.entity.SuperHero;

@Repository
public interface SuperHeroRepository extends JpaRepository<SuperHero, UUID> {

}
