package me.moonways.endpoint.gui.component.function;

import me.moonways.endpoint.gui.component.BnmgComponentLine;
import org.jetbrains.annotations.NotNull;

public class MatrixFunction implements BnmgComponentLineFunction<Integer> {

    private static final String DATA_SEPARATOR = ",";
    private static final String SIGNATURE_REGEX = "[0-9],[0-9]";

    private void validateSignature(String signature) {
        if (signature == null) {
            throw new NullPointerException("matrix signature");
        }

        if (!signature.matches(SIGNATURE_REGEX)) {
            throw new IllegalArgumentException("matrix signature");
        }
    }

    private int toSlot(int x, int y) {
        return ((y - 1) * 9 + x);
    }

    @NotNull
    @Override
    public Integer callFunction(@NotNull BnmgComponentLine line, @NotNull String signature) {
        validateSignature(signature);

        String[] separated = signature.split(DATA_SEPARATOR, 2);

        int matrixX = Integer.parseInt(separated[0]);
        int matrixY = Integer.parseInt(separated[1]);

        return toSlot(matrixX, matrixY);
    }
}
