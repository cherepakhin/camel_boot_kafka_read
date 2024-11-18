package com.behl.searcher.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageBodyLoggerSecond implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		log.info("Exchange.body: {}", exchange.getMessage().getBody());
		log.info("exchange.getProperties: {}", exchange.getProperties());
		log(exchange.getMessage());
	}

	private void log(final Object messageBody) {
		log.info("MessageBodyLoggerSecond information: {}", messageBody);
	}

}