package gr.iserm.java.camel.kai_tutorial;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Based on camel tutorial, http://www.kai-waehner.de/blog/2012/05/04/apache-camel-tutorial-introduction/
 */
public class Main {

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();

		context.addRoutes(new IntegrationRoute());

		context.start();

		Thread.sleep(30000);

		context.stop();
	}
}
