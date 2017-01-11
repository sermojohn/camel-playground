package gr.iserm.java.camel.pollenrich;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by SERMETZI on 15/6/2016.
 */
public class PollEnrichRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer://myTimer?repeatCount=2")
                .to("seda:poll");

        from("seda:poll?concurrentConsumers=2")
                .log("msg ${exchangeId} at ${date:now:HH:mm:ss} will pollenrich.")
                .pollEnrich().simple("direct:pollInput?block=true")
                .timeout(20000)
                .log("msg ${exchangeId} reached the end at ${date:now:HH:mm:ss}.");

        from("timer://myTimer2?delay=10000&repeatCount=2")
            .to("direct:pollInput");


    }
}
