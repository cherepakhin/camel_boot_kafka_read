package ru.perm.v.camel.simple_kafka.producer.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;

@Component
@Slf4j
public class MessageBodyLogger implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		log(exchange.getMessage().getBody());
	}

	private void log(final Object messageBody) {
		if( messageBody.getClass().equals(MessageDTO.class)) {
			MessageDTO  dto = (MessageDTO) messageBody;
			log.info("Message information as MessageDTO: {}", dto);
		} else {
			log.info("Message information: {}", messageBody);
		}
	}

}