package me.moonways.bridgenet.service.bnmg;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.service.bnmg.util.file.session.FileSession;
import me.moonways.bridgenet.service.bnmg.util.file.session.SessionFlag;
import me.moonways.bridgenet.service.bnmg.util.file.session.SessionType;
import me.moonways.bridgenet.service.bnmg.descriptor.GuiDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class BridgenetMinecraftGui {

    private final File file;
    private GuiDescriptor descriptor;

    public GuiDescriptor injectDescriptor() {
        List<String> fileContentLinesList;
        try (FileSession fileSession = FileSession.newBuilder()
                .setFile(file)
                .setType(SessionType.READ_ONLY)
                .addFlags(SessionFlag.FLUSH_AFTER_CLOSING, SessionFlag.PRELOAD_FILE_CONTENT)
                .build()) {

            fileContentLinesList = fileSession.readAllLines();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        throw new UnsupportedOperationException();
    }
}
