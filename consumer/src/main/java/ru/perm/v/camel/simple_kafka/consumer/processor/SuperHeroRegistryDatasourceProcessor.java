package ru.perm.v.camel.simple_kafka.consumer.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import ru.perm.v.camel.simple_kafka.consumer.entity.SuperHero;
import ru.perm.v.camel.simple_kafka.consumer.repository.SuperHeroRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuperHeroRegistryDatasourceProcessor implements Processor {

	private final SuperHeroRepository superHeroRepository;

	@Override
	public void process(final Exchange exchange) {
		superHeroRepository.save((SuperHero) exchange.getMessage().getBody());
	}

}
