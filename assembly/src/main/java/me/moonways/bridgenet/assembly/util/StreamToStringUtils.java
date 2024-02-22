package me.moonways.bridgenet.assembly.util;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.assembly.BridgenetAssemblyException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

@UtilityClass
public class StreamToStringUtils {

    private static final Charset STANDARD_CHARSET = Charset.defaultCharset();

    public byte[] toBytesBySize(InputStream inputStream) {
        try {
            return new byte[inputStream.available()];
        } catch (IOException exception) {
            throw new BridgenetAssemblyException("can not convert java.io.InputStream to bytes array", exception);
        }
    }

    public String toStringFull(InputStream inputStream, Charset charset) {
        try {
            byte[] array = toBytesBySize(inputStream);
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(array);

            return new String(array, charset);
        } catch (IOException exception) {
            throw new BridgenetAssemblyException("can not parse java.io.InputStream to java.lang.String", exception);
        }
    }

    public String toStringFull(InputStream inputStream) {
        return toStringFull(inputStream, STANDARD_CHARSET);
    }

    public String toStringParted(InputStream inputStream, Charset charset, int maxLinesToRead) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream, charset.name());

        for (int i = 0; i < maxLinesToRead; i++) {
            if (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine()).append("\n");
            } else break;
        }

        return stringBuilder.toString();
    }

    public String toStringParted(InputStream inputStream, int maxLinesToRead) {
        return toStringParted(inputStream, STANDARD_CHARSET, maxLinesToRead);
    }
}
