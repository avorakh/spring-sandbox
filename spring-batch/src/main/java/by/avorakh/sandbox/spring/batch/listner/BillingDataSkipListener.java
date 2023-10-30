package by.avorakh.sandbox.spring.batch.listner;


import by.avorakh.sandbox.spring.batch.entity.BillingData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BillingDataSkipListener implements SkipListener<BillingData, BillingData> {

    @NotNull Path skippedItemsFile;

    public BillingDataSkipListener(@NotNull String skippedItemsFile) {
        this(Paths.get(skippedItemsFile));
    }

    @Override
    public void onSkipInRead(@NotNull Throwable throwable) {
        if (throwable instanceof FlatFileParseException exception) {
            var rawLine = exception.getInput();
            int lineNumber = exception.getLineNumber();
            var skippedLine = lineNumber + "|" + rawLine + System.lineSeparator();
            try {
                Files.writeString(this.skippedItemsFile, skippedLine, StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write skipped item " + skippedLine);
            }
        }
    }
}