package io.bootify.my_gate_visitor_management_project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info=@Info(title="Visitor Management Project APIs"))
@SpringBootApplication
public class MyGateVisitorManagementProjectApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MyGateVisitorManagementProjectApplication.class, args);
    }

}
