package by.avorakh.sandbox.spring.batch.svc;

import by.avorakh.sandbox.spring.batch.exception.PricingException;
import by.avorakh.sandbox.spring.batch.util.RandomUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PricingService {

    @Value("${spring.cellular.pricing.data:0.01}")
    float dataPricing;

    @Getter
    @Value("${spring.cellular.pricing.call:0.5}")
    float callPricing;

    @Getter
    @Value("${spring.cellular.pricing.sms:0.1}")
    float smsPricing;
    public float getDataPricing() {
        // to simulate errors
        if (RandomUtils .nextInt(1000) % 7 == 0) {
            throw new PricingException("Error while retrieving data pricing");
        }
        return this.dataPricing;
    }
}
