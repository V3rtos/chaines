package me.moonways.bridgenet.service.bnmg.util.file.session;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public final class FileSessionBuilder {

    private File file;
    private SessionType sessionType;

    private final Set<SessionFlag> sessionFlagSet = new HashSet<>();

    private void validateFile() {
        if (file == null) {
            throw new NullPointerException("file");
        }
    }

    private void validateType() {
        if (sessionType == null) {
            throw new NullPointerException("type");
        }
    }

    private void validateFlagNull(SessionFlag sessionFlag) {
        if (sessionFlag == null) {
            throw new NullPointerException("flag");
        }
    }

    public FileSessionBuilder setFile(@NotNull File file) {
        this.file = file;
        return this;
    }

    public FileSessionBuilder setFile(@NotNull Path path) {
        this.file = path.toFile();
        return this;
    }

    public FileSessionBuilder setType(@NotNull SessionType sessionType) {
        this.sessionType = sessionType;
        return this;
    }

    public FileSessionBuilder addFlag(@NotNull SessionFlag sessionFlag) {
        validateFlagNull(sessionFlag);
        sessionFlagSet.add(sessionFlag);
        return this;
    }

    public FileSessionBuilder addFlags(@NotNull SessionFlag... sessionFlags) {
        for (SessionFlag sessionFlag : sessionFlags) {

            validateFlagNull(sessionFlag);
            sessionFlagSet.add(sessionFlag);
        }
        return this;
    }

    public FileSession build() {
        validateFile();
        validateType();

        FileSession fileSession = new FileSession(file, sessionFlagSet);
        fileSession.init(sessionType);

        return fileSession;
    }
}
