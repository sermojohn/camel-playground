package gr.iserm.java.camel.myflow.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by SERMETZI on 5/4/2016.
 */
public class HeavyProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(HeavyProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		LOG.debug("processor started. ");
		Thread.sleep(5000);
		LOG.debug("processor finished.");

	}

}
