package me.moonways.endpoint.settings;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.entity.persistence.DatabaseEntity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;
import me.moonways.bridgenet.model.settings.Setting;

import java.rmi.RemoteException;
import java.util.UUID;

@Builder
@ToString
@EqualsAndHashCode
@DatabaseEntity(name = "players_settings")
public class EntitySetting {

    public static EntitySetting fromSetting(UUID playerID, Setting<?> setting) throws RemoteException {
        return EntitySetting.builder()
                .playerId(playerID)
                .settingId(setting.id().getId())
                .value(setting.get())
                .build();
    }

    @Getter(onMethod_ = @EntityParameter(order = 1, id = "player_id"))
    private UUID playerId;
    @Getter(onMethod_ = @EntityParameter(order = 2, id = "setting_id"))
    private UUID settingId;

    @Getter(onMethod_ = @EntityParameter(order = 3, id = "setting_value"))
    private Object value;
}
