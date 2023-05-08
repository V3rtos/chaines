package me.moonways.bridgenet.transfer;

import me.moonways.bridgenet.transfer.provider.TransferBufferProvider;
import me.moonways.bridgenet.transfer.provider.TransferProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteTransfer {

    Class<? extends TransferProvider> provider() default TransferBufferProvider.class;
}
