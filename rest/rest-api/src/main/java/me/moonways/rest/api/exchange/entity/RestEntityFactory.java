package me.moonways.rest.api.exchange.entity;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.rest.api.exchange.entity.type.FileEntity;
import me.moonways.rest.api.exchange.entity.type.JsonEntity;
import me.moonways.rest.api.exchange.entity.type.MultipartEntity;
import me.moonways.rest.api.exchange.entity.type.TextEntity;

import java.io.File;
import java.nio.file.Path;

@Component
public final class RestEntityFactory {

    @Inject
    private Gson gson;

    public ExchangeableEntity createTextEntity(String text) {
        return new TextEntity(text);
    }

    public ExchangeableEntity createFileEntity(File file) {
        return new FileEntity(file);
    }

    public ExchangeableEntity createFileEntity(Path path) {
        return new FileEntity(path.toFile());
    }

    public ExchangeableEntity createJsonEntity(Object object) {
        return new JsonEntity(gson.toJson(object));
    }

    public MultipartEntity.Builder createMultipartEntity() {
        return MultipartEntity.newMultipartBuilder();
    }
}
