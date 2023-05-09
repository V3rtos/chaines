package me.moonways.bridgenet.api.util.file.session;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FileSession implements Closeable {

    public static FileSessionBuilder newBuilder() {
        return new FileSessionBuilder();
    }

    private final File file;
    private final Set<SessionFlag> sessionFlagSet;

    private FileInputStream in;
    private FileOutputStream out;

    private byte[] preloadedDataArray;

    private boolean hasFlag(SessionFlag sessionFlag) {
        return sessionFlagSet.contains(sessionFlag);
    }

    public void init(@NotNull SessionType sessionType) {
        createFile();
        preload();

        initSessionType(sessionType);
    }

    @SneakyThrows({FileNotFoundException.class})
    private void initSessionType(@NotNull SessionType sessionType) {
        switch (sessionType) {
            case READ_ONLY:
                in = new FileInputStream(file);
                break;

            case WRITE_ONLY:
                out = new FileOutputStream(file);
                break;

            case READ_AND_WRITE:
                in = new FileInputStream(file);
                out = new FileOutputStream(file);
        }
    }

    @SneakyThrows({IOException.class})
    private void createFile() {
        if (!file.exists() && hasFlag(SessionFlag.CREATE_IF_NOT_EXISTS))
            Files.createFile(file.toPath());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    private void preload() {
        if (!hasFlag(SessionFlag.PRELOAD_FILE_CONTENT)) {
            preloadedDataArray = new byte[0];
        }
        else {
            if (in == null) {
                try (FileInputStream fileInputStream = new FileInputStream(file)) {

                    preloadedDataArray = new byte[(int) file.length()];
                    fileInputStream.read(preloadedDataArray);
                }
            }
            else {
                preloadedDataArray = new byte[in.available()];
                in.read(preloadedDataArray);
            }
        }
    }

    @SneakyThrows({IOException.class})
    private void validateInputAccess() {
        if (in == null || in.available() == 0) {
            throw new IOException("File input stream cannot be accessed");
        }
    }

    @SneakyThrows({IOException.class})
    private void validateOutputAccess() {
        if (out == null) {
            throw new IOException("File input stream cannot be accessed");
        }
    }

    @SneakyThrows({IOException.class})
    public synchronized void write(byte[] arr) {
        validateOutputAccess();

        out.write(arr);
        if (!hasFlag(SessionFlag.FLUSH_AFTER_CLOSING))
            out.flush();
    }

    @SneakyThrows({IOException.class})
    public synchronized void write(byte[] arr, int offset, int length) {
        validateOutputAccess();

        out.write(arr, offset, length);
        if (!hasFlag(SessionFlag.FLUSH_AFTER_CLOSING))
            out.flush();
    }

    public synchronized void write(String string) {
        write(string.getBytes());
    }

    public synchronized void write(Charset charset, String string) {
        write(string.getBytes(charset));
    }

    public synchronized void writef(String string, Object... parameters) {
        String formattedString = String.format(string, parameters);
        write(formattedString);
    }

    public synchronized void writef(Charset charset, String string, Object... parameters) {
        String formattedString = String.format(string, parameters);
        write(charset, formattedString);
    }

    public synchronized void writeln(String string) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        write(stringNewLine.getBytes());
    }

    public synchronized void writeln(Charset charset, String string) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        write(stringNewLine.getBytes(charset));
    }

    public synchronized void writefln(String string, Object... parameters) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        String formattedString = String.format(stringNewLine, parameters);

        write(formattedString);
    }

    public synchronized void writefln(Charset charset, String string, Object... parameters) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        String formattedString = String.format(stringNewLine, parameters);

        write(charset, formattedString);
    }

    public synchronized void put(byte[] arr) {
        validateOutputAccess();

        boolean isPreloadedNotEmpty = preloadedDataArray.length > 0;
        boolean hasPreloadFlag = hasFlag(SessionFlag.PRELOAD_FILE_CONTENT);

        if (isPreloadedNotEmpty && hasPreloadFlag) {

            int totalLength = arr.length + preloadedDataArray.length;
            byte[] newArray = new byte[totalLength];

            for (int index = 0; index < newArray.length; index++) {
                if (index >= preloadedDataArray.length)
                    newArray[index] = arr[index - preloadedDataArray.length];
                else
                    newArray[index] = preloadedDataArray[index];
            }

            arr = newArray;
        }

        write(arr);
    }

    public synchronized void put(String string) {
        put(string.getBytes());
    }

    public synchronized void put(Charset charset, String string) {
        put(string.getBytes(charset));
    }

    public synchronized void putf(String string, Object... parameters) {
        String formattedString = String.format(string, parameters);
        put(formattedString);
    }

    public synchronized void putf(Charset charset, String string, Object... parameters) {
        String formattedString = String.format(string, parameters);
        put(charset, formattedString);
    }

    public synchronized void putln(String string) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        put(stringNewLine.getBytes());
    }

    public synchronized void putln(Charset charset, String string) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        put(stringNewLine.getBytes(charset));
    }

    public synchronized void putfln(String string, Object... parameters) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        String formattedString = String.format(stringNewLine, parameters);

        put(formattedString);
    }

    public synchronized void putfln(Charset charset, String string, Object... parameters) {
        String newlinePrefix = (preloadedDataArray.length > 0 ? "\n" : "");
        String stringNewLine = newlinePrefix.concat(string);

        String formattedString = String.format(stringNewLine, parameters);

        put(charset, formattedString);
    }

    @SneakyThrows({IOException.class})
    public synchronized int read(byte[] arr) {
        validateInputAccess();
        return in.read(arr);
    }

    @SneakyThrows({IOException.class})
    public synchronized int read(byte[] arr, int offset, int length) {
        validateInputAccess();
        return in.read(arr, offset, length);
    }

    @SneakyThrows({IOException.class})
    public synchronized byte[] readAll() {
        validateInputAccess();

        byte[] arr = new byte[in.available()];
        read(arr);

        return arr;
    }

    public synchronized String readAllString() {
        return new String(readAll());
    }

    public synchronized String readAllString(Charset charset) {
        return new String(readAll(), charset);
    }

    public synchronized List<String> readAllLines() {
        return Arrays.stream(readAllString().split("\n")).collect(Collectors.toList());
    }

    public synchronized List<String> readAllLines(Charset charset) {
        return Arrays.stream(readAllString(charset).split("\n")).collect(Collectors.toList());
    }

    @Override
    public synchronized void close() throws IOException {
        if (in != null)
            in.close();

        if (out != null) {
            if (hasFlag(SessionFlag.FLUSH_AFTER_CLOSING))
                out.flush();

            out.close();
        }
    }
}
