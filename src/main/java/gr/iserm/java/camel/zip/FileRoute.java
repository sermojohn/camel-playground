package gr.iserm.java.camel.zip;

import org.apache.camel.builder.RouteBuilder;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by sermojohn on 5/4/2016.
 */
public class FileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:src/data/zip?noop=true")
                .process(new FileProcessor())
                    .to("file:src/data/zip-output/");
    }

}
