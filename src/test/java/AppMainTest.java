import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

public class AppMainTest {

    private static final long DURATION_MILLIS = 10_000;
    private static final String SOURCE_FOLDER = "src/test/source-folder";
    private static final String DESTINATION_FOLDER = "src/test/destination-folder";

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

}
