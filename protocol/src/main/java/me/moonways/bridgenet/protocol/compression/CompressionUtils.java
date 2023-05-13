package me.moonways.bridgenet.protocol.compression;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Log4j2
@UtilityClass
public class CompressionUtils {

    private static final byte HAS_COMPRESSION_STATE = 1;
    private static final byte NO_COMPRESSION_STATE = 0;

    public byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        deflater.finish();
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {

            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }

    public byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!inflater.finished()) {

            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }

    public void write(byte[] array, ByteBuf byteBuf) throws IOException {
        byte[] compressedBytes = compress(array);

        boolean hasCompression = array.length > compressedBytes.length;

        byteBuf.writeByte(hasCompression ? HAS_COMPRESSION_STATE : NO_COMPRESSION_STATE);
        byteBuf.writeBytes(hasCompression ? compressedBytes : array);
    }

    public byte[] read(ByteBuf byteBuf) throws IOException, DataFormatException {
        byte state = byteBuf.readByte();
        byte[] array = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(array);

        if (state == HAS_COMPRESSION_STATE)
            return decompress(array);

        return array;
    }
}