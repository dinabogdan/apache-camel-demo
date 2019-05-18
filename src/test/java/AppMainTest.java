import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;
import org.postgresql.jdbc2.optional.SimpleDataSource;

import javax.sql.DataSource;

public class AppMainTest {

    private static final long DURATION_MILLIS = 10_000;
    private static final String SOURCE_FOLDER = "src/test/source-folder";
    private static final String DESTINATION_FOLDER = "src/test/destination-folder";
    private static final String DB_URL = "jdbc:postgresql://192.168.99.100:5432/postgres";
    private static final String CUSTOM_DATA_SOURCE = "customDataSource";

    @Test
    public void moveFolderContent() throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://" + SOURCE_FOLDER + "?delete=true")
                        .process(new FileProcessor())
                        .to("file://" + DESTINATION_FOLDER);
            }
        });
        camelContext.addRoutes(new CustomRouteBuilder());
        camelContext.start();
        Thread.sleep(DURATION_MILLIS);
        camelContext.stop();
    }

    @Test
    public void readContentFromDbCamelContext() throws Exception {
        DataSource dataSource = setupDataSource();
        SimpleRegistry simpleRegistry = new SimpleRegistry();

        simpleRegistry.put(CUSTOM_DATA_SOURCE, dataSource);
        CamelContext camelContext = new DefaultCamelContext(simpleRegistry);

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:customer")
                        .to("log:?level=INFO&showBody=true")
                        .setBody(constant("select * from customer"))
                        .to("jdbc:" + CUSTOM_DATA_SOURCE)
                        .process(new CustomerProcessor())
                        .to("file://C:/DevTools/workspace/apache-camel/?fileName=customers.txt&charset=utf-8");
            }
        });


        camelContext.start();

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.request("direct:customer", null);

        Thread.sleep(5_000);

        camelContext.stop();
    }

    private SimpleDataSource setupDataSource() {
        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("Password");
        dataSource.setUrl(DB_URL);
        return dataSource;
    }

    private class Customer {
        public int id;
        public String name;
        public int age;
    }

}
