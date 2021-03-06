package gr.iserm.java.camel.kai_tutorial;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by SERMETZI on 1/4/2016.
 */
public class IntegrationRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:src/data/inbox?noop=true")
			.setHeader("testHeader", simple("testValue"))

			.process(new LoggingProcessor())

			.bean(new TransformationBean(), "makeUpperCase")

			.unmarshal().csv()
			.split(body().tokenize(","))
				.choice()
					.when(body().contains("DVD"))
						.to("log:DVD")
						.process(new LoggingProcessor())
					.when(body().contains("CD"))
						.to("log:CD")
						.process(new LoggingProcessor())
					.otherwise()
						.to("log:OTHERS")
						.process(new LoggingProcessor())
			;
	}
}
