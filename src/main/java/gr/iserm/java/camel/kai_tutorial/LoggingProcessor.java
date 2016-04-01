package gr.iserm.java.camel.kai_tutorial;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LoggingProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Received Order: " + exchange.getIn().getBody(String.class));
	}

}
