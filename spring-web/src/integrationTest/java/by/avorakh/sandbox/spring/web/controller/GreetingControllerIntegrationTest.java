package by.avorakh.sandbox.spring.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import by.avorakh.sandbox.spring.web.resource.Greeting;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.util.UriComponentsBuilder;

public class GreetingControllerIntegrationTest extends SpringIntegrationTest {

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {
        var response = template.getForEntity("/greeting", Greeting.class);

        assertNotNull(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertNotNull(response.getBody());
        assertThat(response.getBody().id()).isPositive();
        assertNotNull(response.getBody().content());
        assertThat(response.getBody().content()).isEqualTo("Hello, World!");
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {

        var url = UriComponentsBuilder.fromHttpUrl(toUrl("/greeting"))
                .queryParam("name", "SomeName")
                .toUriString();

        var response = template.getForEntity(url, Greeting.class);

        assertNotNull(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertNotNull(response.getBody());
        assertThat(response.getBody().id()).isPositive();
        assertNotNull(response.getBody().content());
        assertThat(response.getBody().content()).isEqualTo("Hello, SomeName!");
    }
}
