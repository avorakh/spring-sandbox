package by.avorakh.sandbox.spring.web.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringIntegrationTest {
    @Autowired
    protected TestRestTemplate template;

    protected @NotNull String toUrl(@NotNull String path) {
        return template.getRootUri().concat(path);
    }
}
