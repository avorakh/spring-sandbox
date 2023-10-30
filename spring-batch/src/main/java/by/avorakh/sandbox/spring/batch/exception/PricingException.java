package by.avorakh.sandbox.spring.batch.exception;

import org.jetbrains.annotations.NotNull;

public class PricingException extends RuntimeException {

    public PricingException(@NotNull String message) {
        super(message);
    }
}