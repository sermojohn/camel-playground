package gr.iserm.java.camel.aggregates;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AggregateRouteBuilder extends RouteBuilder {

	private static final String INBOX_HEADER = "header1";
	private static final String INBOX_COMPLETE_HEADER = "header2";

	@Override
	public void configure() throws Exception {
		from("timer://myTimer?period=1000")
			.process(new Processor() {
				private int counter = 0;
				@Override
				public void process(Exchange exchange) throws Exception {
					counter++;

					exchange.getIn().setBody("body_"+counter);
					if(counter % 5 == 0) {
						exchange.getIn().setHeader(INBOX_COMPLETE_HEADER, true);
						exchange.getIn().setHeader(INBOX_HEADER, "batch_"+(Math.floor(counter/5)));
					} else {
						exchange.getIn().setHeader(INBOX_HEADER, "batch_"+(Math.floor(counter/5)+1));
					}
				}
			})
			.to("seda:inbox");


		from("seda:inbox")
			.aggregate(new MyAggregationStrategy()).header(INBOX_HEADER)
				.completionPredicate(simple("${header."+INBOX_COMPLETE_HEADER+"}"))
			.split(body()).process(exchange -> {
				Exchange originalExchange = exchange.getIn().getBody(Exchange.class);
				exchange.getOut().setBody(originalExchange.getIn().getBody());
				exchange.getOut().setHeaders(originalExchange.getIn().getHeaders());
			})
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn();
				}
			})
			.log("message ${body} reached the end.");
	}

	private class MyAggregationStrategy implements AggregationStrategy {
		@Override
		public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
			List<Exchange> body = (oldExchange == null) ? new ArrayList<>() : oldExchange.getIn().getBody(List.class);
			body.add(newExchange);

			DefaultExchange aggExchange = new DefaultExchange(getContext());
			aggExchange.getIn().setBody(body);
			aggExchange.getIn().setHeader(INBOX_COMPLETE_HEADER, newExchange.getIn().getHeader(INBOX_COMPLETE_HEADER));
			return aggExchange;
		}
	}
}
