package me.moonways.endpoint.settings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.model.settings.Setting;
import me.moonways.bridgenet.model.settings.SettingID;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PlayerSettings {

    private final Map<SettingID<?>, Setting<?>> settings;
    private final UUID playerID;

    @SuppressWarnings("unchecked")
    public <T> Setting<T> get(SettingID<T> id) throws RemoteException {
        if (settings.containsKey(id)) {
            return (Setting<T>) settings.get(id);
        }
        return create(id);
    }

    private <T> Setting<T> create(SettingID<T> id) throws RemoteException {
        Setting<T> setting = new SettingStub<>(id);
        settings.put(id, setting);

        doInsert(setting);

        return setting;
    }

    public void loadAll() {
        // todo: fill settings map from entity-repository
    }

    private void doInsert(Setting<?> setting) throws RemoteException {
        SettingPair settingPair = SettingPair.builder()
                .playerId(playerID)
                .settingId(setting.id().getId())
                .value(null)
                .build();

        // todo - insert `settingPair` to entity-repository.
    }
}
