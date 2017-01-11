package gr.iserm.java.camel.errorhandling;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.io.IOException;

public class AdvancedErrorHandlingExample extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start3")
                // route specific error handler that is different than the global error handler
                // here we do not redeliver and send errors to mock:error3 instead of the global endpoint
                .errorHandler(deadLetterChannel("mock:error3").maximumRedeliveries(0))

                // route specific on exception to mark MyFunctionalException as being handled
                .onException(IllegalArgumentException.class).handled(true).end()
                // however we want the IO exceptions to redeliver at most 3 times
                .onException(IOException.class).maximumRedeliveries(3).end()
                .to("log:result");

        from("direct:start")
            .errorHandler(defaultErrorHandler().onExceptionOccurred(exchange -> exchange.getProperty(Exchange.EXCEPTION_CAUGHT)))
            .onException(RuntimeException.class).handled(true).wireTap("log:wiretap").end().end()
            .to("log:end1")
            .choice()
                .when(simple("true"))
                    .throwException(new RuntimeException())
                    .to("log:end2")
                .otherwise()
                    .to("log:end3");

        from("timer://myTimer?repeatCount=1&delay=0")
                .setBody(constant("hello world!"))
                .to("direct:start");
    }
}
