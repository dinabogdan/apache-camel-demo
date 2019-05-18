package com.freesoft.router;

import com.freesoft.router.processor.FooProcessor;
import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.Exchange.FILE_NAME;

public class ContentBasedRouter extends RouteBuilder {

    private static ContentBasedRouter INSTANCE = null;

    private final String source;
    private final String fooDestination;
    private final String barDestination;
    private final String destination;

    private ContentBasedRouter(String source, String fooDestination, String barDestination, String destination) {
        this.source = source;
        this.fooDestination = fooDestination;
        this.barDestination = barDestination;
        this.destination = destination;
    }

    public synchronized static ContentBasedRouter instance(String source,
                                                           String fooDestination,
                                                           String barDestination,
                                                           String destination) {
        if (INSTANCE == null) {
            INSTANCE = new ContentBasedRouter(source, fooDestination, barDestination, destination);
        }
        return INSTANCE;

    }

    @Override
    public void configure() throws Exception {
        from(source)
                .choice()
                .when(header(FILE_NAME).contains("foo"))
                .process(FooProcessor.instance())
                .to(fooDestination)
                .when(header(FILE_NAME).contains("bar"))
                .to(barDestination)
                .otherwise().to(destination);
    }
}
