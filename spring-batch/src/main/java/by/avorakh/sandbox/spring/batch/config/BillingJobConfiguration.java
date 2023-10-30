package by.avorakh.sandbox.spring.batch.config;

import by.avorakh.sandbox.spring.batch.entity.BillingData;
import by.avorakh.sandbox.spring.batch.entity.ReportingData;
import by.avorakh.sandbox.spring.batch.exception.PricingException;
import by.avorakh.sandbox.spring.batch.listner.BillingDataSkipListener;
import by.avorakh.sandbox.spring.batch.processor.BillingDataProcessor;
import by.avorakh.sandbox.spring.batch.step.FilePreparationTasklet;
import by.avorakh.sandbox.spring.batch.svc.PricingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.DataClassRowMapper;

import javax.sql.DataSource;

@Configuration
public class BillingJobConfiguration {

    public static final String INTO_BILLING_DATA = "insert into BILLING_DATA values (:dataYear, :dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, :smsCount)";
    public static final String SELECT_FROM_BILLING_DATA_BY_YEAR_AND_MONTH = "select * from BILLING_DATA where DATA_YEAR = %d and DATA_MONTH = %d";

    @Bean
    public @NotNull Step step1(
            @NotNull JobRepository jobRepository,
            @NotNull JdbcTransactionManager transactionManager
    ) {
        return new StepBuilder("filePreparation", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public @NotNull Step step2(
            @NotNull JobRepository jobRepository,
            @NotNull JdbcTransactionManager transactionManager,
            @NotNull ItemReader<BillingData> billingDataFileReader,
            @NotNull ItemWriter<BillingData> billingDataTableWriter,
            @NotNull SkipListener<BillingData, BillingData> skipListener
    ) {
        return new StepBuilder("fileIngestion", jobRepository)
                .<BillingData, BillingData>chunk(100, transactionManager)
                .reader(billingDataFileReader)
                .writer(billingDataTableWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(10)
                .listener(skipListener)
                .build();
    }

    @Bean
    public @NotNull Step step3(
            @NotNull JobRepository jobRepository,
            @NotNull JdbcTransactionManager transactionManager,
            @NotNull ItemReader<BillingData> billingDataTableReader,
            @NotNull ItemProcessor<BillingData, ReportingData> billingDataProcessor,
            @NotNull ItemWriter<ReportingData> billingDataFileWriter
    ) {
        return new StepBuilder("reportGeneration", jobRepository)
                .<BillingData, ReportingData>chunk(100, transactionManager)
                .reader(billingDataTableReader)
                .processor(billingDataProcessor)
                .writer(billingDataFileWriter)
                .faultTolerant()
                .retry(PricingException.class)
                .retryLimit(100)
                .build();
    }

    @Bean
    public @NotNull Job job(
            @NotNull JobRepository jobRepository,
            @NotNull Step step1,
            @NotNull Step step2,
            @NotNull Step step3
    ) {
        return new JobBuilder("BillingJob", jobRepository)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    @StepScope
    public @NotNull FlatFileItemReader<BillingData> billingDataFileReader(
            @NotNull @Value("#{jobParameters['input.file']}") String inputFile
    ) {
        return new FlatFileItemReaderBuilder<BillingData>()
                .name("billingDataFileReader")
                .resource(new FileSystemResource(inputFile))
                .delimited()
                .names("dataYear", "dataMonth", "accountId", "phoneNumber", "dataUsage", "callDuration", "smsCount")
                .targetType(BillingData.class)
                .build();
    }

    @Bean
    public @NotNull JdbcBatchItemWriter<BillingData> billingDataTableWriter(@NotNull DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<BillingData>()
                .dataSource(dataSource)
                .sql(INTO_BILLING_DATA)
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public @NotNull JdbcCursorItemReader<BillingData> billingDataTableReader(
            @NotNull DataSource dataSource,
            @NotNull @Value("#{jobParameters['data.year']}") Integer year,
            @NotNull @Value("#{jobParameters['data.month']}") Integer month
    ) {
        return new JdbcCursorItemReaderBuilder<BillingData>()
                .name("billingDataTableReader")
                .dataSource(dataSource)
                .sql(String.format(SELECT_FROM_BILLING_DATA_BY_YEAR_AND_MONTH, year, month))
                .rowMapper(new DataClassRowMapper<>(BillingData.class))
                .build();
    }

    @Bean
    public @NotNull ItemProcessor<BillingData, ReportingData> billingDataProcessor(
            @NotNull PricingService pricingService
    ) {
        return new BillingDataProcessor(pricingService);
    }

    @Bean
    @StepScope
    public @NotNull FlatFileItemWriter<ReportingData> billingDataFileWriter(
            @Value("#{jobParameters['output.file']}") String outputFile
    ) {
        return new FlatFileItemWriterBuilder<ReportingData>()
                .resource(new FileSystemResource(outputFile))
                .name("billingDataFileWriter")
                .delimited()
                .names("billingData.dataYear", "billingData.dataMonth", "billingData.accountId", "billingData.phoneNumber", "billingData.dataUsage", "billingData.callDuration", "billingData.smsCount", "billingTotal")
                .build();
    }

    @Bean
    @StepScope
    public @NotNull SkipListener<BillingData, BillingData> skipListener(
            @NotNull @Value("#{jobParameters['skip.file']}") String skippedFile
    ) {
        return new BillingDataSkipListener(skippedFile);
    }

    @Bean
    public @NotNull PricingService pricingService() {
        return new PricingService();
    }
}