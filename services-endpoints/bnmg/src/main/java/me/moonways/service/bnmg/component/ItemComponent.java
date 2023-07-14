package me.moonways.service.bnmg.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.moonways.service.bnmg.component.function.BnmgComponentLineFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ItemComponent extends AbstractBnmgComponent {

    private static final int ITEM_LINE_SLOT_INDEX = 1;
    private static final int ITEM_LINE_TYPE_INDEX = 2;
    private static final int ITEM_LINE_DATA_INDEX = 3;
    private static final int ITEM_LINE_AMOUNT_INDEX = 4;
    private static final int ITEM_LINE_GLOW_INDEX = 5;
    private static final int ITEM_LINE_NAME_INDEX = 6;
    private static final int ITEM_LINE_LORE_INDEX = 7;
    private static final int ITEM_LINE_ACTIONS_INDEX = 8;

    public ItemComponent(String name) {
        super(name, new BnmgComponentLine[8]);
    }

    public void setSlot(@NotNull BnmgComponentLineFunction<Integer> function, @NotNull String slot) {
        setComponentLine(ITEM_LINE_TYPE_INDEX, function.callFunction(lines[ITEM_LINE_SLOT_INDEX], slot));
    }

    public void setType(@NotNull String type) {
        setComponentLine(ITEM_LINE_TYPE_INDEX, type);
    }

    public void setData(int data) {
        setComponentLine(ITEM_LINE_DATA_INDEX, data);
    }

    public void setAmount(int amount) {
        setComponentLine(ITEM_LINE_AMOUNT_INDEX, amount);
    }

    public void setActions(@NotNull List<String> actions) {
        setComponentLine(ITEM_LINE_ACTIONS_INDEX, actions);
    }

    public void setItemName(@NotNull String name) {
        setComponentLine(ITEM_LINE_NAME_INDEX, name);
    }

    public void setLore(@NotNull List<String> lore) {
        setComponentLine(ITEM_LINE_LORE_INDEX, lore);
    }

    public void setGlowing(boolean flag) {
        setComponentLine(ITEM_LINE_GLOW_INDEX, flag ? 1 : 0);
    }

    public int getSlot(@NotNull BnmgComponentLineFunction<Integer> function) {
        return getComponentContent(ITEM_LINE_SLOT_INDEX, function);
    }

    public String getType() {
        return getComponentContent(ITEM_LINE_TYPE_INDEX);
    }

    public int getData() {
        return getComponentContentMapping(ITEM_LINE_DATA_INDEX, Integer::parseInt);
    }

    public int getAmount() {
        return getComponentContentMapping(ITEM_LINE_AMOUNT_INDEX, Integer::parseInt);
    }

    public Collection<String> getActions() {
        return getComponentContentList(ITEM_LINE_ACTIONS_INDEX);
    }

    public String getItemName() {
        return getComponentContent(ITEM_LINE_NAME_INDEX);
    }

    public Collection<String> getLore() {
        return getComponentContentList(ITEM_LINE_LORE_INDEX);
    }

    public boolean isGlowing() {
        return getComponentContentMapping(ITEM_LINE_AMOUNT_INDEX, (content) -> Integer.parseInt(content) == 1 ? Boolean.TRUE : Boolean.FALSE);
    }
}
