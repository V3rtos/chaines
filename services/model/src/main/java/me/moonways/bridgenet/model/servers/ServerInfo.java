package me.moonways.bridgenet.model.servers;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class ServerInfo {

    private final InetSocketAddress address;

    @EqualsAndHashCode.Include
    private final String name;

    private final List<ServerFlag> flags;

    /**
     * Проверить наличие установленного флага на сервере.
     * @param flag - проверяемый флаг.
     */
    public boolean hasFlag(@NotNull ServerFlag flag) {
        return flags.contains(flag);
    }
}
