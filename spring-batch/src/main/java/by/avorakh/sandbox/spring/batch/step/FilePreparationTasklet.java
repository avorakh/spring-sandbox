package by.avorakh.sandbox.spring.batch.step;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FilePreparationTasklet implements Tasklet {

    @Override
    public @NotNull RepeatStatus execute(@NotNull StepContribution contribution, ChunkContext chunkContext) throws Exception {
        var jobParameters = contribution.getStepExecution().getJobParameters();
        var inputFile = jobParameters.getString("input.file");
        var source = Paths.get(inputFile);
        var target = Paths.get("staging", source.toFile().getName());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return RepeatStatus.FINISHED;
    }
}
