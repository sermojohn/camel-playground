package gr.iserm.java.camel.zip;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

import java.io.File;

public class ZipRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:src/data/zip?noop=true&idempotent=true")
            .setHeader("parentFolder", simple("tmp-" + System.currentTimeMillis()))
            .split(new ZipSplitter()).streaming()
                .choice()
                    .when(body().isNotNull())
                        .to("file:src/data/zip-output/?fileName=" + simple("${in.header[parentFolder]}" + File.separator + "${in.header[CamelFileName]}").getText());
    }

}
