package me.moonways.endpoint.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.model.gui.Gui;
import me.moonways.bridgenet.model.gui.GuiDescription;
import me.moonways.bridgenet.model.gui.GuiSlot;
import me.moonways.bridgenet.model.gui.click.ItemClickEvent;
import me.moonways.bridgenet.model.gui.click.ItemClickListener;
import me.moonways.bridgenet.model.gui.item.ItemStack;

import java.rmi.RemoteException;
import java.util.*;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class GuiStub implements Gui {

    @ToString.Include
    private final UUID id;
    @ToString.Include
    private final GuiDescription description;

    @ToString.Include
    private final Map<GuiSlot, ItemStack> contentMap = new HashMap<>();
    private final Map<GuiSlot, List<ItemClickListener>> clickListenersMap = new HashMap<>();
    private final List<ItemClickListener> totalsClickListeners = new ArrayList<>();

    private void addContent(GuiSlot slot, ItemStack itemStack, ItemClickListener clickListener) {
        removeContent(slot);
        if (itemStack != null) {
            contentMap.put(slot, itemStack);
        }

        addClickContent(slot, clickListener);
    }

    private void addClickContent(GuiSlot slot, ItemClickListener clickListener) {
        if (clickListener != null) {
            List<ItemClickListener> itemClickListeners = clickListenersMap.get(slot);

            if (itemClickListeners == null) {
                itemClickListeners = new ArrayList<>();
            }
            itemClickListeners.add(clickListener);
        }
    }

    private void removeContent(GuiSlot slot) {
        contentMap.remove(slot);
        clickListenersMap.remove(slot);
    }

    public void fireClick(ItemClickEvent event) {
        clickListenersMap.values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(listener -> listener.consume(event));

        totalsClickListeners.forEach(listener -> listener.consume(event));
    }

    @Override
    public ItemStack getItem(GuiSlot slot) throws RemoteException {
        return contentMap.get(slot);
    }

    @Override
    public void setItem(GuiSlot slot, ItemStack itemStack, ItemClickListener clickListener) throws RemoteException {
        addContent(slot, itemStack, clickListener);
    }

    @Override
    public void setItem(GuiSlot slot, ItemStack itemStack) throws RemoteException {
        addContent(slot, itemStack, null);
    }

    @Override
    public void removeItem(GuiSlot slot) throws RemoteException {
        removeContent(slot);
    }

    @Override
    public void addClickListener(ItemClickListener listener) throws RemoteException {
        totalsClickListeners.add(listener);
    }

    @Override
    public void addClickListener(GuiSlot slot, ItemClickListener listener) throws RemoteException {
        addClickContent(slot, listener);
    }
}
