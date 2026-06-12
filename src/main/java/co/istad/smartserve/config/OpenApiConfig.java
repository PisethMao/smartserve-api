package co.istad.smartserve.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI smartServeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartServe RESTful APIs")
                        .version("v1.0.0")
                        .description("RESTful APIs documentation for SmartServe restaurant management system.")
                        .contact(new Contact()
                                .name("Piseth Mao")
                                .email("pisethmao2002@gmail.com")
                        )
                        .license(new License()
                                .name("SmartServe Internal Project")));
    }
}
