package gr.iserm.java.camel.myflow.routers;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by SERMETZI on 5/4/2016.
 */
public class TimerRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("timer://myTimer?period=2000")
				.setBody().simple("Current time is ${header.firedTime}")
				.to("direct:theBeginning");
	}
}
