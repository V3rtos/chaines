package me.moonways.endpoint.gui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.moonways.bridgenet.model.service.gui.Gui;
import me.moonways.bridgenet.model.service.gui.GuiDescription;
import me.moonways.bridgenet.model.service.gui.GuiType;
import me.moonways.bridgenet.model.event.GuiItemClickEvent;
import me.moonways.bridgenet.model.service.gui.item.Items;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.bridgenet.model.service.gui.GuiServiceModel;

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
    public void fireClickAction(UUID guiId, GuiItemClickEvent event) throws RemoteException {
        guisCache.cleanUp();

        GuiStub guiStub = guisCache.getIfPresent(guiId);

        if (guiStub != null) {
            guiStub.fireClick(event);
        }
    }

    @Override
    public void fireClickAction(Gui gui, GuiItemClickEvent event) throws RemoteException {
        fireClickAction(gui.getId(), event);
    }
}
