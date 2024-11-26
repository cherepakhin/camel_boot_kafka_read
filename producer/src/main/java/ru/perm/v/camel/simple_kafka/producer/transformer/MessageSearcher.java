package ru.perm.v.camel.simple_kafka.producer.transformer;

import org.springframework.stereotype.Component;

import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;

@Component
public class MessageSearcher {

	public MessageDTO search() {
		return MessageDTO.build();
	}

}
