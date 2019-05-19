package com.freesoft.router.processor;

import com.freesoft.Customer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomerProcessor implements Processor {

    private static CustomerProcessor INSTANCE = null;

    public static Processor instance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerProcessor();
        }
        return INSTANCE;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        List<Map<String, Object>> body = exchange.getIn().getBody(List.class);
        StringBuilder stringBuilder = new StringBuilder();
        Consumer<String> consumerAppender = customer -> stringBuilder.append(customer).append("\n");
        body.stream().map(customerConverter()).forEach(consumerAppender);
        exchange.getIn().setBody(stringBuilder.toString());
    }

    private Function<Map<String, Object>, String> customerConverter() {
        return map -> {
            int id = (int) map.get("id");
            String name = (String) map.get("name");
            int age = (int) map.get("age");

            String[] nameParts = name.split(" ");

            Customer customer = new Customer(id, age, nameParts[0], nameParts[1]);

            return customer.toString();
        };
    }
}
