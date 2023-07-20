package me.moonways.bridgenet.mtp.config;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public final class MTPConfiguration {

    private static final String CONFIG_FILENAME = "mtp_settings.json";

    @Getter
    private Settings settings;

    @Getter
    private MessageEncryption encryption;

    @Inject
    private Gson gson;

    @Synchronized
    public void reload() {
        String configurationContent = readContent();

        settings = gson.fromJson(configurationContent, Settings.class);
        encryption = new MessageEncryption(settings.getSecurity());

        log.info("Json configuration parsed from {}", CONFIG_FILENAME);
    }

    @SuppressWarnings({"DataFlowIssue", "resource", "ResultOfMethodCallIgnored"})
    @SneakyThrows
    private String readContent() {
        Path path = Paths.get(CONFIG_FILENAME);

        if (!Files.exists(path)) {

            InputStream inputStream = MTPConfiguration.class.getResourceAsStream("/" + CONFIG_FILENAME);
            byte[] arr = new byte[inputStream.available()];

            inputStream.read(arr);

            return new String(arr);
        }

        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
