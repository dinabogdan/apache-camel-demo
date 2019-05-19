package com.freesoft;

import com.freesoft.router.ContentBasedRouter;
import com.freesoft.router.MessageTranslatorRouter;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.postgresql.jdbc2.optional.SimpleDataSource;

public class Main {

    private static final String SOURCE = "file://src/resources/source";
    private static final String DESTINATION = "file://src/resources/destination";
    private static final String FOO_DESTINATION = "file://src/resources/foo-destination";
    private static final String BAR_DESTINATION = "file://src/resources/bar-destination";

    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Password";
    private static final String DB_URL = "jdbc:postgresql://192.168.99.100:5432/postgres";
    private static final String DATASOURCE_NAME = "customerDataSource";
    private static final String URI = "direct:customerTable";
    private static final String CUSTOMER_DESTINATION = "file://C:/DevTools/workspace/apache-camel/?fileName=customers.txt&charset=utf-8";
    private static final String SQL_QUERY = "select * from customer";

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.start();

        SimpleRegistry simpleRegistry = new SimpleRegistry();
        simpleRegistry.put(DATASOURCE_NAME, customerDataSource());

        ((DefaultCamelContext) camelContext).setRegistry(simpleRegistry);

        camelContext.addRoutes(ContentBasedRouter.instance(SOURCE, FOO_DESTINATION, BAR_DESTINATION, DESTINATION));
        camelContext.addRoutes(MessageTranslatorRouter.instance(DATASOURCE_NAME, URI, CUSTOMER_DESTINATION, SQL_QUERY));

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.request(URI, null);

        Thread.sleep(10_000);
        camelContext.stop();
    }

    private static SimpleDataSource customerDataSource() {
        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUser(DB_USER);
        simpleDataSource.setPassword(DB_PASSWORD);
        simpleDataSource.setUrl(DB_URL);
        return simpleDataSource;
    }
}
