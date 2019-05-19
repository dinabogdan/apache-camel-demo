package com.freesoft.router;

import com.freesoft.router.processor.CustomerProcessor;
import org.apache.camel.builder.RouteBuilder;

public class MessageTranslatorRouter extends RouteBuilder {

    private static MessageTranslatorRouter INSTANCE = null;

    private final String dataSource;
    private final String uri;
    private final String destination;
    private final String command;


    private MessageTranslatorRouter(String dataSource, String uri, String destination, String command) {
        this.dataSource = dataSource;
        this.uri = uri;
        this.destination = destination;
        this.command = command;
    }

    public synchronized static MessageTranslatorRouter instance(String dataSource,
                                                                String uri,
                                                                String destination,
                                                                String command) {
        if (INSTANCE == null) {
            INSTANCE = new MessageTranslatorRouter(dataSource, uri, destination, command);
        }
        return INSTANCE;
    }

    @Override
    public void configure() throws Exception {
        from(uri)
                .to("log:?level=INFO&showBody=true")
                .setBody(constant(command))
                .to("jdbc:" + dataSource)
                .process(CustomerProcessor.instance())
                .to(destination);
    }
}
