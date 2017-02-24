package gr.iserm.java.camel.so;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import static java.lang.System.out;

/**
 * Created by iserm on 23/2/2017.
 */
public class SoQARouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer://myTimer?period=2000")
                .to("direct:from");

        from("direct:from").process(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("Hello");
            }
        }).to("direct:one").process(exchange -> out.println("process output: "+exchange.getIn().getBody()));

        from("direct:one").setBody(constant("Second To Fourth Endpoint")).to("file://target/inbox");
    }
}
