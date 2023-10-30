package by.avorakh.sandbox.spring.batch.processor;

import by.avorakh.sandbox.spring.batch.entity.BillingData;
import by.avorakh.sandbox.spring.batch.entity.ReportingData;
import by.avorakh.sandbox.spring.batch.svc.PricingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BillingDataProcessor implements ItemProcessor<BillingData, ReportingData> {

    @NotNull PricingService pricingService;

    @NonFinal
    @Value("${spring.cellular.spending.threshold:150}")
    float spendingThreshold;


    @Override
    public @Nullable ReportingData process(@NotNull BillingData item) {
        double billingTotal =
                item.dataUsage() * pricingService.getDataPricing() +
                        item.callDuration() * pricingService.getCallPricing() +
                        item.smsCount() * pricingService.getSmsPricing();
        if (billingTotal < spendingThreshold) {
            return null;
        }
        return new ReportingData(item, billingTotal);
    }
}