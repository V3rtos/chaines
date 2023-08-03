package me.moonways.rest.api.exchange.entity.type;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.rest.api.exchange.entity.EntityWriter;
import me.moonways.rest.api.exchange.entity.ExchangeableEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class MultipartEntity implements ExchangeableEntity {

    public static Builder newMultipartBuilder() {
        return new Builder();
    }

    public static class Builder {

        private final List<ExchangeableEntity> entities = new ArrayList<>();

        public Builder addPart(ExchangeableEntity entity) {
            entities.add(entity);
            return this;
        }

        public MultipartEntity build() {
            return new MultipartEntity(entities);
        }
    }

    @ToString.Include
    private List<ExchangeableEntity> entities;

    @Override
    public void setContent(Object object) {
        if (!(object instanceof Collection)) {
            return;
        }

        entities = ((Collection<?>) object).stream().map(o -> (ExchangeableEntity) o)
                .collect(Collectors.toList());
    }

    @Override
    public void write(@NotNull EntityWriter entityWriter) {
        for (ExchangeableEntity entity : entities) {

            entityWriter.write(entity);
            entityWriter.writeCRLF();
        }
    }

    @Override
    public byte[] toByteArray() {
        byte[] crlfBytes = EntityWriter.CRLF.getBytes();

        int totalSize = entities.stream().mapToInt(value -> value.toByteArray().length + crlfBytes.length).sum();
        byte[] totalBytes = new byte[totalSize];

        int lastPos = 0;

        for (ExchangeableEntity entity : entities) {
            byte[] entityBytes = entity.toByteArray();

            System.arraycopy(totalBytes, lastPos, entityBytes, entityBytes.length,
                    (lastPos += entityBytes.length));
            System.arraycopy(totalBytes, lastPos, crlfBytes, crlfBytes.length,
                    (lastPos += crlfBytes.length));
        }

        return totalBytes;
    }
}
