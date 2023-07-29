package me.moonways.bridgenet.rest.api.exchange.entity;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.api.exception.RestEntityException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@RequiredArgsConstructor
public final class EntityWriter {

    private static final String LN = "\n";
    public static final String CRLF = "\r\n";

    private final ByteArrayOutputStream container = new ByteArrayOutputStream();

    private void write(byte[] arr) {
        try {
            container.write(arr);
        }
        catch (IOException exception) {
            throw new RestEntityException(exception);
        }
    }

    public void write(String string) {
        write(string.getBytes());
    }

    public void write(String string, Charset charset) {
        write(string.getBytes(charset));
    }

    public void write(ExchangeableEntity entity) {
        write(entity.toByteArray());
    }

    public void writeLN() {
        write(LN);
    }

    public void writeCRLF() {
        write(CRLF);
    }

    public byte[] toByteArray() {
        return container.toByteArray();
    }
}
