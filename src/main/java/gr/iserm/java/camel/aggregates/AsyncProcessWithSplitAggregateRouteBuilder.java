package gr.iserm.java.camel.aggregates;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;

public class AsyncProcessWithSplitAggregateRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:inbox")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn();
					}
				})
				.split(simple("${body.getItems}")).parallelProcessing()
					.to("seda:itemProcess")
				.end();

		from("seda:itemProcess")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						Thread.sleep(2000);
					}
				})
				.to("log:processed")
				.aggregate(new UseLatestAggregationStrategy()).simple("body.parentId").completionSize(exchangeProperty("CamelSplitSize"))
				.to("log:final");
	}
}
