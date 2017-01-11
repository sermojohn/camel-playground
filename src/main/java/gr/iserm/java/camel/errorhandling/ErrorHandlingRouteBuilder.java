package gr.iserm.java.camel.errorhandling;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.DefaultErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.processor.RedeliveryPolicy;
import org.apache.camel.util.RouteStatDump;

import java.nio.channels.NoConnectionPendingException;

public class ErrorHandlingRouteBuilder extends RouteBuilder {

	private static final String POSITION_HEADER = "POSITION";

	@Override
	public void configure() throws Exception {

		DefaultErrorHandlerBuilder defaultErrorHandlerBuilder = defaultErrorHandler().logStackTrace(false);
		//DefaultErrorHandlerBuilder defaultErrorHandlerBuilder = deadLetterChannel("direct:DLC")
				/*.maximumRedeliveries(2)
				.redeliveryDelay(1000)
				.retryAttemptedLogLevel(LoggingLevel.WARN)*/
				;

		/*onException(IllegalArgumentException.class, NullPointerException.class, NoConnectionPendingException.class).handled(true)
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn();
					}
				})
				.bean(new ExceptionHandlingBeanImpl(), "handleException(${body}, ${exception})").to("seda:output");
		onException(Exception.class).handled(true)
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn();
					}
				});*/

		from("timer://myTimer?period=1000&repeatCount=2")
			.threads(10)
			.inOut("seda:preInput")
			.to("seda:output");

		from("seda:preInput").routeId("originatingRoute")
			.inOut("seda:input");

		from("seda:input")
			.inOut("direct:identityProcessing")
			.inOut("direct:helloProcessing")
			.inOut("direct:exceptionProcessing");

		from("seda:output")
			.log("message [${in.header.POSITION}] successfully processed. $Exception: ${property.CamelExceptionCaught}");

		from("direct:identityProcessing").routeId("identityRoute")
			.errorHandler(defaultErrorHandler().maximumRedeliveries(0))
			.delay(2000)
			.process(new IdentityProcessor());


		from("direct:helloProcessing").routeId("helloRoute")
			.errorHandler(defaultErrorHandlerBuilder)
			.delay(2000)
			.process(new HelloProcessor());

		from("direct:exceptionProcessing").routeId("exceptionRoute")
			.errorHandler(defaultErrorHandlerBuilder)
			.delay(2000)
			.process(new ExceptionProcessor());

		from("direct:DLC")
		.log("Message [${in.header.POSITION}] failed with [${property.CamelExceptionCaught}] at route [${property.CamelFailureRouteId}]");

		from("timer://myTimer?period=5000").routeId("checkCanStartNextIndexingProcessCalculation")
				.inOut("controlbus:route?routeId=originatingRoute&action=stats")
				.transform(xpath("/routeStat/@exchangesInflight"))
				.choice()
					.when(simple("${body} > 0"))
						.setBody(constant(Boolean.FALSE))
					.otherwise()
						.setBody(constant(Boolean.TRUE))
				.end();

	}

	private static class IdentityProcessor implements Processor {
		private static int counter = 0;
		@Override
		public void process(Exchange exchange) throws Exception {
			if(exchange.getIn().getHeader(POSITION_HEADER) == null) {
				exchange.getIn().setHeader(POSITION_HEADER, counter++);
				exchange.getIn().setBody("Hello world!");
			}
		}
	}


	private static class HelloProcessor implements Processor {
		@Override
		public void process(Exchange exchange) throws Exception {
			int position = (Integer) exchange.getIn().getHeader(POSITION_HEADER);
			/*if(position % 8 == 0) {
				throw new IllegalArgumentException("More specific exception for message with position "+position+" and ID["+exchange.getIn().getMessageId()+"]");
			}
			if(position % 4 == 0) {
				throw new RuntimeException("Exception for message with position "+position+" and ID["+exchange.getIn().getMessageId()+"]");
			}*/
		}
	}

	private static class ExceptionProcessor implements Processor {

		@Override
		public void process(Exchange exchange) throws Exception {
			int position = (Integer) exchange.getIn().getHeader(POSITION_HEADER);
			/*if(position % 3 == 0) {
				throw new RuntimeException("Exception for message with position "+position+" and ID["+exchange.getIn().getMessageId()+"]");
			}*/
		}

	}

}
