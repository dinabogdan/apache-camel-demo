import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.Exchange.FILE_NAME;

public class CustomRouteBuilder extends RouteBuilder {

    private static final String SOURCE_FOLDER = "file://src/test/other-source-folder";
    private static final String FOO_DESTINATION_FOLDER = "file://src/test/foo";
    private static final String BAR_DESTINATION_FOLDER = "file://src/test/bar";
    private static final String DESTINATION_FOLDER = "file://src/test/destination-folder";

    @Override
    public void configure() throws Exception {
        from(SOURCE_FOLDER)
                .choice()
                .when(header(FILE_NAME).contains("foo"))
                .to(FOO_DESTINATION_FOLDER)
                .when(header(FILE_NAME).contains("bar"))
                .to(BAR_DESTINATION_FOLDER)
                .otherwise().to(DESTINATION_FOLDER);
    }
}
