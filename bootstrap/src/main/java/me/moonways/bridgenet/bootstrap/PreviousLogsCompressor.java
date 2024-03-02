package me.moonways.bridgenet.bootstrap;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.assembly.util.FilesZipCompressionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class PreviousLogsCompressor {

    private static final SimpleDateFormat LOGS_ARCHIVE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyy_HH-mm-ss");

    public void compressToGzip() {
        Path logsPath = Paths.get("logs");
        if (!Files.exists(logsPath)) {
            return;
        }

        try (Stream<Path> filesList = Files.list(logsPath)) {
            List<File> previousLogFilesList = filesList
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".log"))
                    .filter(file -> file.length() > 0)
                    .collect(Collectors.toList());

            if (!previousLogFilesList.isEmpty()) {

                String archiveName = String.format("%s-logs", LOGS_ARCHIVE_DATE_FORMAT.format(System.currentTimeMillis()));
                FilesZipCompressionUtils.compressToZip(logsPath, archiveName, previousLogFilesList.toArray(new File[0]));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
