package gr.iserm.java.camel.defaulterrors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by sermetzi on 2/6/2016.
 */
public class DefaultErrorHandlerRouting extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer://myTimer?period=1000&repeatCount=10")
                .to("direct:destination");

        from("direct:destination")
                .threads(5).threadName("pollAttachment").maxPoolSize(6)
                    .log(simple("msg started ${exchangeId} ${date:now:HH:mm:ss}").getText())
                    .delay(10000)
                    .log("msg finished ${exchangeId} ${date:now:HH:mm:ss}");

    }


}
