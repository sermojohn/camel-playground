package gr.iserm.java.camel.myflow.routers;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by SERMETZI on 5/4/2016.
 */
public class QuarzRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("quartz://myTimer?cron=0/5+*+*+*+*+?")
				.setBody().simple("Current time is ${header.firedTime}")
				.to("log:foo");
	}

}
