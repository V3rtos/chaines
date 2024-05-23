package me.moonways.bridgenet.rest.api.exchange.entity.type;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rest.api.exception.RestEntityException;
import me.moonways.bridgenet.rest.api.exchange.entity.EntityWriter;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity implements ExchangeableEntity {

    private File content;

    @Override
    public void setContent(Object object) {
        boolean isFile = (object instanceof File);
        boolean isPath = (object instanceof Path);

        if (!isFile && !isPath) {
            return;
        }

        this.content = (isFile ? ((File) object) : ((Path) object).toFile());
    }

    @Override
    public void write(@NotNull EntityWriter entityWriter) {
        entityWriter.write(this);
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Files.copy(content.toPath(), outputStream);
        } catch (IOException exception) {
            throw new RestEntityException(exception);
        }

        return outputStream.toByteArray();
    }
}
