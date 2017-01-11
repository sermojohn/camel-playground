package gr.iserm.java.camel.looper;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.util.Random;

public class LoopRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("timer://myTimer?period=2000")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						String[] values = {"A", "B", "C", "D"};
						exchange.getIn().setBody(values[new Random().nextInt(values.length-1)]);
					}
				})
				.loopDoWhile(new Predicate() {
					@Override
					public boolean matches(Exchange exchange) {
						return ((String)exchange.getIn().getBody()).length() < 5;
					}
				})
					.delay(100)
					.process(new Processor() {
						@Override
						public void process(Exchange exchange) throws Exception {
							String body = (String) exchange.getIn().getBody();
							exchange.getIn().setBody(body + body);
						}
					})
				.end()
				.to("log:theend");


	}
}
