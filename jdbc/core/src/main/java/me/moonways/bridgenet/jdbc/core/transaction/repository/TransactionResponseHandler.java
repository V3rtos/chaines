package me.moonways.bridgenet.jdbc.core.transaction.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.ResponseStream;

import java.util.function.Consumer;

@Getter
@Builder
@ToString
public class TransactionResponseHandler {

    private Consumer<ResponseStream> ifPresent;
    private Runnable ifNoPresent;
}
