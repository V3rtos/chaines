package me.moonways.endpoint.gui.component.function;

import me.moonways.endpoint.gui.component.BnmgComponentLine;
import org.jetbrains.annotations.NotNull;

public class SlotFunction implements BnmgComponentLineFunction<Integer> {

    private static final String SIGNATURE_REGEX = "[0-9]+";

    private void validateSignature(String signature) {
        if (signature == null) {
            throw new NullPointerException("slot function signature");
        }

        if (!signature.matches(SIGNATURE_REGEX)) {
            throw new IllegalArgumentException("slot function signature");
        }
    }

    @NotNull
    @Override
    public Integer callFunction(@NotNull BnmgComponentLine line, @NotNull String signature) {
        validateSignature(signature);
        return Integer.parseInt(signature);
    }
}
