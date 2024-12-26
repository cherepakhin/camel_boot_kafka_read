package ru.perm.v.camel.simple_kafka.producer;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamelProducerApplication {

	public static void main(String[] args) {
		//1.Create CamelContext
		CamelContext camelContext = new DefaultCamelContext();

		//2.Create Routes by extending RouteBuilder
//		camelContext.addRoutes(new ApacheCamelRoutes());

		//3.Start the CamelContext
		camelContext.start();

		SpringApplication.run(CamelProducerApplication.class, args);
	}

}
