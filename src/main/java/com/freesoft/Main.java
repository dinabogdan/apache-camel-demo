package com.freesoft;

import com.freesoft.router.ContentBasedRouter;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class Main {

    private static final String SOURCE = "file://src/resources/source";
    private static final String DESTINATION = "file://src/resources/destination";
    private static final String FOO_DESTINATION = "file://src/resources/foo-destination";
    private static final String BAR_DESTINATION = "file://src/resources/bar-destination";

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.start();

        camelContext.addRoutes(ContentBasedRouter.instance(SOURCE, FOO_DESTINATION, BAR_DESTINATION, DESTINATION));

        Thread.sleep(10_000);
        camelContext.stop();
    }
}
