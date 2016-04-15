package gr.iserm.java.camel.myflow.routers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class MulticastRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {

		from("timer://myTimer?period=2000")
				.process(new Processor() {
					private int counter;
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn().setBody("Hello world");
					}
				})
				.filter(simple("${body} == 'Hello world'"))
				.setHeader("test", simple("${body}"))
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn();
					}
				})
				.multicast()
				.to("direct:output1").to("direct:output2");

		from("direct:output1")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn();
					}
				})
				.log("message received in output1 ${body}");

		from("direct:output2")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn();
					}
				})
				.log("message received in output2 ${body}");



	}
}
