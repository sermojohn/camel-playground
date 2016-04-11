package gr.iserm.java.camel.aggregates;

import gr.iserm.java.camel.aggregates.models.Item;
import gr.iserm.java.camel.aggregates.models.ProcessingCycle;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultProducerTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();

		context.addRoutes(new AsyncProcessWithSplitAggregateRouteBuilder());

		context.start();

		ProducerTemplate producerTemplate = new DefaultProducerTemplate(context);
		producerTemplate.start();
		producerTemplate.sendBody("direct:inbox", buildProcessingCycle("1"));
		producerTemplate.stop();

		Thread.sleep(60000);

		context.stop();
	}

	private static ProcessingCycle buildProcessingCycle(String processingCycleId) {

		ProcessingCycle processingCycle = new ProcessingCycle(processingCycleId, new Date());
		processingCycle.addItem(new Item("1", "Name 1"));
		processingCycle.addItem(new Item("2", "Name 2"));
		processingCycle.addItem(new Item("3", "Name 3"));
		return processingCycle;

	}
}
