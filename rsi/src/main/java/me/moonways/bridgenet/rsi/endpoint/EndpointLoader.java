package me.moonways.bridgenet.rsi.endpoint;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class EndpointLoader {

    private static final String LOCAL_BUILD_DIR = ".build/services";
    private static final String DEDICATED_BUILD_DIR = "services";
    private static final String CONFIG_FILE_NAME = "endpoint_config.json";

    @Inject
    private RemoteServiceRegistry remoteServiceRegistry;

    @Inject
    private Gson gson;

    private EndpointConfig parseJsonConfig(@NotNull String endpointName, @NotNull File file) {
        if (!Files.exists(file.toPath())) {
            log.error("§4Json config for '{}' endpoint is not found", endpointName);
            return null;
        }

        try {
            String jsonContent = String.join("", Files.readAllLines(file.toPath()));
            return gson.fromJson(jsonContent, EndpointConfig.class);
        }
        catch (IOException exception) {
            log.error("§4Cannot be inject endpoint config of '{}': §c{}", endpointName, exception.toString());
            return null;
        }
    }

    public Endpoint loadEndpoint(@NotNull File endpointFolder, @NotNull ServiceInfo serviceInfo) {
        String endpointName = serviceInfo.getName();

        File endpointConfigFile = endpointFolder.toPath().resolve(CONFIG_FILE_NAME).toFile();
        EndpointConfig endpointConfig = parseJsonConfig(endpointName, endpointConfigFile);

        return new Endpoint(serviceInfo, endpointFolder.toPath(), endpointConfig);
    }

    private boolean isDedicatedLoading() {
        Path buildDirectoryPath = Paths.get(correctPath(LOCAL_BUILD_DIR));
        return !Files.exists(buildDirectoryPath);
    }

    private String correctPath(String pathname) {
        return pathname.replace("/", File.separator);
    }

    private ServiceInfo findServiceInfo(String name) {
        return remoteServiceRegistry.getServicesInfos().get(name.toLowerCase());
    }

    @SuppressWarnings("resource")
    public List<Endpoint> lookupStoredEndpoints() {
        String servicesContentPathname = isDedicatedLoading() ? DEDICATED_BUILD_DIR : LOCAL_BUILD_DIR;
        Path servicesContentPath = Paths.get(servicesContentPathname);

        if (!Files.exists(servicesContentPath) || !Files.isDirectory(servicesContentPath)) {
            log.warn("§4Directory '{}' for services content was not found", servicesContentPathname);
            return Collections.emptyList();
        }

        try {
            List<Endpoint> list = new ArrayList<>();
            Files.list(servicesContentPath)
                    .forEachOrdered(endpointPath -> {
                        if (Files.isDirectory(endpointPath)) {

                            String name = endpointPath.toFile().getName();
                            ServiceInfo serviceInfo = findServiceInfo(name);

                            if (serviceInfo == null) {
                                log.warn("§4Directory '{}' not loaded as endpoint", name);
                                return;
                            }

                            Endpoint endpoint = loadEndpoint(endpointPath.toFile(), serviceInfo);

                            list.add(endpoint);
                        }
                    });

            return list;
        } catch (IOException exception) {
            log.error("§4Cannot be lookup endpoints content: §c{}", exception.toString());
        }

        return Collections.emptyList();
    }
}
