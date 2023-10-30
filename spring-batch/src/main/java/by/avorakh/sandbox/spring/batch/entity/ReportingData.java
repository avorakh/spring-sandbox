package by.avorakh.sandbox.spring.batch.entity;

public record ReportingData(
        BillingData billingData,
        double billingTotal
) {
}