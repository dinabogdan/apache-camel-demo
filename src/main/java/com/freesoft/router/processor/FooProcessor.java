package com.freesoft.router.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.time.LocalDateTime;

import static org.apache.camel.Exchange.FILE_NAME;

public class FooProcessor implements Processor {

    private static FooProcessor INSTANCE;

    private FooProcessor() {

    }

    public synchronized static FooProcessor instance() {
        if (INSTANCE == null) {
            INSTANCE = new FooProcessor();
        }

        return INSTANCE;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = exchange.getIn().getHeader(FILE_NAME).toString();
        fileName = fileName + "_bar_" + LocalDateTime.now().toString();
        exchange.getIn().setHeader(FILE_NAME, fileName);
    }
}
