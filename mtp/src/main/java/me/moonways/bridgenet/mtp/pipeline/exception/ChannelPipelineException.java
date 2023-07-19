package me.moonways.bridgenet.mtp.pipeline.exception;

import org.jetbrains.annotations.NotNull;

public class ChannelPipelineException extends RuntimeException {

    private static final long serialVersionUID = -6764387138018072816L;

    public ChannelPipelineException(@NotNull Throwable throwable, @NotNull String  message) {
        super(message, throwable);
    }
}
