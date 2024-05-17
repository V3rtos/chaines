package me.moonways.endpoint.gui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.moonways.bridgenet.model.gui.Gui;
import me.moonways.bridgenet.model.gui.GuiDescription;
import me.moonways.bridgenet.model.gui.GuiType;
import me.moonways.bridgenet.model.gui.click.ItemClickEvent;
import me.moonways.bridgenet.model.gui.item.Items;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.bridgenet.model.gui.GuiServiceModel;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class GuiServiceEndpoint extends EndpointRemoteObject implements GuiServiceModel {

    private static final long serialVersionUID = -698118002154484440L;
    
    private final Cache<UUID, GuiStub> guisCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(1, TimeUnit.HOURS)
                    .build();

    @Getter
    private final Items items = new ItemsStub();

    public GuiServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    public Gui createGui(GuiType type) throws RemoteException {
        return createGui(
                GuiDescription.builder()
                        .title("")
                        .type(type)
                        .size(type.getSize())
                        .build()
        );
    }

    @Override
    public Gui createGui(GuiDescription description) throws RemoteException {
        GuiStub guiStub = new GuiStub(UUID.randomUUID(), description);
        guisCache.put(guiStub.getId(), guiStub);
        return guiStub;
    }

    @Override
    public Optional<Gui> getGui(UUID id) {
        guisCache.cleanUp();
        return Optional.ofNullable(guisCache.getIfPresent(id));
    }

    @Override
    public void fireClickAction(UUID guiId, ItemClickEvent event) throws RemoteException {
        guisCache.cleanUp();

        GuiStub guiStub = guisCache.getIfPresent(guiId);

        if (guiStub != null) {
            guiStub.fireClick(event);
        }
    }

    @Override
    public void fireClickAction(Gui gui, ItemClickEvent event) throws RemoteException {
        fireClickAction(gui.getId(), event);
    }

    /*
     *
     * ItemStack(amount=0, durability=0, material=Material(namespace=minecraft:diamond_block, entry=MaterialEntry(id=90, maxStackSize=0, maxDamage=0, isFood=false, translationKey=block.minecraft.diamond_block, correspondingBlock=minecraft:diamond_block, eatingSound=null, drinkingSound=null)), name=Tested item stack, lore=null, flags=null, enchantments=null)
     * ItemStack(amount=1, durability=0, material=Material(namespace=minecraft:diamond_block, entry=MaterialEntry(id=90, maxStackSize=0, maxDamage=0, isFood=false, translationKey=block.minecraft.diamond_block, correspondingBlock=minecraft:diamond_block, eatingSound=null, drinkingSound=null)), name=Tested item stack, lore=null, flags=null, enchantments=null)
     */
}
