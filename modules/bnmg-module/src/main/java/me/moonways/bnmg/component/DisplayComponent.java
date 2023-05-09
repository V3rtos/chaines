package me.moonways.bnmg.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DisplayComponent extends AbstractBnmgComponent {

    private static final int DISPLAY_LINE_TITLE_INDEX = 1;
    private static final int DISPLAY_LINE_TYPE_INDEX = 2;
    private static final int DISPLAY_LINE_ROWS_INDEX = 3;

    public DisplayComponent(String name) {
        super(name, new BnmgComponentLine[3]);
    }

    public void setTitle(@NotNull String title) {
        setComponentLine(DISPLAY_LINE_TITLE_INDEX, title);
    }

    public void setType(@NotNull String type) {
        setComponentLine(DISPLAY_LINE_TYPE_INDEX, type);
    }

    public void setRows(int rows) {
        setComponentLine(DISPLAY_LINE_ROWS_INDEX, rows);
    }

    public String getTitle() {
        return getComponentContent(DISPLAY_LINE_TITLE_INDEX);
    }

    public String getType() {
        return getComponentContent(DISPLAY_LINE_TYPE_INDEX);
    }

    public int getRows() {
        return getComponentContentMapping(DISPLAY_LINE_ROWS_INDEX, Integer::parseInt);
    }
}
