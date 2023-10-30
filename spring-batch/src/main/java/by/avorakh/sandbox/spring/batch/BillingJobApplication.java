package by.avorakh.sandbox.spring.batch;


import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingJobApplication {
    public static void main(String @NotNull [] args) {
        SpringApplication.run(BillingJobApplication.class, args);
    }
}
