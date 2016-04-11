package gr.iserm.java.camel.aggregates;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;

public class AggregateRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:inbox")
			.aggregate(new UseLatestAggregationStrategy()).header("bundleId").completionSize(3)
		.to("log:foo");
	}
}
