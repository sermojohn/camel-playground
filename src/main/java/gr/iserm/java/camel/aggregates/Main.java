package gr.iserm.java.camel.aggregates;

import gr.iserm.java.camel.aggregates.models.Item;
import gr.iserm.java.camel.aggregates.models.ProcessingCycle;

import java.util.Date;

public class Main {

	public static void main(String[] args) throws Exception {
		org.apache.camel.main.Main main = new org.apache.camel.main.Main();

		/*PropertiesComponent propertiesComponent = new PropertiesComponent();
		Properties properties = new Properties();
		properties.setProperty("consumers", "5");
		propertiesComponent.setOverrideProperties(properties);
		main.addComponent("properties", propertiesComponent);*/

		//context.addRoutes(new AsyncProcessWithSplitAggregateRouteBuilder());
		main.addRouteBuilder(new AggregateRouteBuilder());

		main.run();

		/*ProducerTemplate producerTemplate = new DefaultProducerTemplate(context);
		producerTemplate.start();
		producerTemplate.sendBody("direct:inbox", buildProcessingCycle("1"));
		producerTemplate.stop();

		Thread.sleep(60000);

		context.stop();*/
	}

	private static ProcessingCycle buildProcessingCycle(String processingCycleId) {

		ProcessingCycle processingCycle = new ProcessingCycle(processingCycleId, new Date());
		processingCycle.addItem(new Item("1", "Name 1"));
		processingCycle.addItem(new Item("2", "Name 2"));
		processingCycle.addItem(new Item("3", "Name 3"));
		return processingCycle;

	}
}
