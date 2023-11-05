package by.avorakh.sandbox.spring.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class HelloControllerIntegrationTest extends ControllerIntegrationTest {

    @Test
    public void getHello() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/", String.class);

        assertNotNull(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(response.getBody()).isEqualTo("Greetings from Spring Boot!");
    }
}
