import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.camel.Exchange.FILE_NAME;

public class FileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String originalFileName = exchange.getIn().getHeader(FILE_NAME, String.class);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String changedFileName = simpleDateFormat.format(date) + originalFileName;
        exchange.getIn().setHeader(FILE_NAME, changedFileName);
    }
}
