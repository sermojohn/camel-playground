package gr.iserm.java.camel.zip;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

public class ZipRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:src/data/zip?noop=true&idempotent=true")
            .log("received message ${id}")
            .setHeader("parentFolder", simple(String.valueOf(System.currentTimeMillis())))
            .split(new ZipSplitter()).streaming()
            .to("file:src/data/zip-output/?fileName=${in.header.parentFolder}/${in.header.CamelFileName}");
    }

}
