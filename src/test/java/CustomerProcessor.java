import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

class Customer {
    int id;
    String name;
    int age;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class CustomerProcessor implements Processor {

    private Function<Map<String, Object>, String> mapToStringFunction;

    @Override
    public void process(Exchange exchange) throws Exception {
        List<Map<String, Object>> body = exchange.getIn().getBody(List.class);
        StringBuilder stringBuilder = new StringBuilder();
        mapToStringFunction = map -> {
            int id = (int) map.get("id");
            String name = (String) map.get("name");
            int age = (int) map.get("age");

            Customer customer = new Customer();
            customer.id = id;
            customer.name = name;
            customer.age = age;

            return customer.toString();
        };
        body.stream()
                .map(mapToStringFunction)
                .forEach(string -> stringBuilder.append(string).append("\n"));
        exchange.getIn().setBody(stringBuilder.toString());

    }
}
