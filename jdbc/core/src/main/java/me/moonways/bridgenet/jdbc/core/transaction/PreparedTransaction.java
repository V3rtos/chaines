package me.moonways.bridgenet.jdbc.core.transaction;

import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import org.jetbrains.annotations.NotNull;

public interface PreparedTransaction {

    TransactionResult call(@NotNull DatabaseConnection connection);
}
