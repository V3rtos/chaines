package me.moonways.bridgenet.api.util.json;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractJsonConfig<Source> {

  private static final Gson GSON = new Gson();

  private final Class<Source> sourceType;
  private final String filename;

  protected abstract void onReloaded(Source source);

  @Synchronized
  public void reload() {
    String configurationContent = readContent();

    Source source = GSON.fromJson(configurationContent, sourceType);
    onReloaded(source);

    log.info("Json configuration parsed from {}", filename);
  }

  @SuppressWarnings({"DataFlowIssue", "resource", "ResultOfMethodCallIgnored"})
  @SneakyThrows
  private String readContent() {
    File file = new File(filename);

    if (!file.exists()) {

      InputStream inputStream = getClass().getResourceAsStream("/" + filename);
      byte[] arr = new byte[inputStream.available()];

      inputStream.read(arr);

      return new String(arr);
    }

    byte[] bytes = Files.readAllBytes(file.toPath());
    return new String(bytes);
  }
}
