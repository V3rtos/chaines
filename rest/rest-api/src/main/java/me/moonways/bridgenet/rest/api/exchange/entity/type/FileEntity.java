package me.moonways.bridgenet.rest.api.exchange.entity.type;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.api.exchange.entity.EntityWriter;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.bridgenet.rest.api.exception.RestEntityException;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
public class FileEntity implements ExchangeableEntity {

    private final File file;

    @Override
    public void write(@NotNull EntityWriter entityWriter) {
        entityWriter.write(this);
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Files.copy(file.toPath(), outputStream);
        }
        catch (IOException exception) {
            throw new RestEntityException(exception);
        }

        return outputStream.toByteArray();
    }
}
