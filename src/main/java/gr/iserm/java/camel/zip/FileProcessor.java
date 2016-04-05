package gr.iserm.java.camel.zip;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.FileEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.InputStream;

/**
 * Created by sermojohn on 5/4/2016.
 */
public class FileProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(FileProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("exchange passed: "+exchange.getIn().getHeader(Exchange.FILE_NAME));
        /*exchange.getOut().setBody("testing");
        exchange.getOut().setHeader("CamelFileName",
                exchange.getIn().getHeader("parentFolder")
                    +File.separator + exchange.getIn().getHeader("CamelFileName"));*/
    }

}
