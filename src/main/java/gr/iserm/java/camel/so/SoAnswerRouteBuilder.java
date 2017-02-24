package gr.iserm.java.camel.so;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by iserm on 24/2/2017.
 */
public class SoAnswerRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer://myTimer?period=2000").to("direct:from");

        from("direct:from").process(exchange -> exchange.getIn().setBody("Bob"))
            .to("direct:step1")
            .to("seda:step2")
            .log("${body}");

        from("direct:step1").process(exchange -> exchange.getOut().setBody("Hi, "+exchange.getIn().getBody()));

        from("seda:step2").process(exchange -> exchange.getOut().setBody(exchange.getIn().getBody()+"!"));


    }

}
