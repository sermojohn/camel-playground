package gr.iserm.java.camel.myflow.routers;

import gr.iserm.java.camel.myflow.processors.HeavyProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by SERMETZI on 5/4/2016.
 */
public class AsyncRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:theBeginning")
				.to("seda:asyncBeginning");

		from("seda:asyncBeginning?concurrentConsumers=5")
				.process(new HeavyProcessor());
	}

}
