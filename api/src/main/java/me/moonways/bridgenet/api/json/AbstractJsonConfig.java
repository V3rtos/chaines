package me.moonways.bridgenet.api.json;

import com.google.gson.Gson;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractJsonConfig<Source> {

  private final Class<Source> sourceType;
  private final String filename;

  @Inject
  private Gson gson;

  protected abstract void onReloaded(Source source);

  @Synchronized
  public void reload() {
    String configurationContent = readContent();

    Source source = gson.fromJson(configurationContent, sourceType);
    onReloaded(source);

    log.info("Json configuration parsed from {}", filename);
  }

  @SuppressWarnings({"DataFlowIssue", "resource", "ResultOfMethodCallIgnored"})
  @SneakyThrows
  private String readContent() {
    Path path = Paths.get(filename);

    if (!Files.exists(path)) {

      InputStream inputStream = getClass().getResourceAsStream("/" + filename);
      byte[] arr = new byte[inputStream.available()];

      inputStream.read(arr);

      return new String(arr);
    }

    byte[] bytes = Files.readAllBytes(path);
    return new String(bytes);
  }
}
