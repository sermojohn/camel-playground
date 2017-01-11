package gr.iserm.java.camel.errorhandling;

import org.apache.camel.builder.RouteBuilder;

public class StackoverflowError extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("log:dlq"));
            //.onExceptionOccurred(exchange -> System.out.println("ERROR: "+exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class))));

        from("direct-vm:start").routeId("start")
                .wireTap("direct-vm:wiretap_exception")
                .to("direct-vm:endroute_last")
        ;

        from("direct-vm:wiretap_exception").routeId("wiretap_exception")
                .process(exchange -> { throw new RuntimeException("I AM EXCEPTION"); });

        from("direct-vm:endroute_last").routeId("endroute_last")
                .process(exchange -> { throw new RuntimeException("I AM ALSO EXCEPTION"); });


        from("timer://myTimer?repeatCount=1&delay=0")
                .setBody(constant("hello world!"))
                .to("direct-vm:start");
    }
}
