package me.moonways.bridgenet.test.services;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.gui.Gui;
import me.moonways.bridgenet.model.gui.GuiDescription;
import me.moonways.bridgenet.model.gui.GuiServiceModel;
import me.moonways.bridgenet.model.gui.GuiType;
import me.moonways.bridgenet.model.gui.click.ItemClickEvent;
import me.moonways.bridgenet.model.gui.item.ItemStack;
import me.moonways.bridgenet.model.gui.item.entries.material.Material;
import me.moonways.bridgenet.model.gui.item.entries.material.MaterialEntry;
import me.moonways.bridgenet.model.gui.item.types.Materials;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class GuiServiceEndpointTest {

    @Inject
    private GuiServiceModel serviceModel;

    @Test
    public void test_materialsGet() {
        Material material = Materials.STONE;
        log.debug(material);

        MaterialEntry stoneEntry = material.entry();

        assertEquals(1, stoneEntry.getId());
        assertEquals("block.minecraft.stone", stoneEntry.getTranslationKey());
        assertEquals("minecraft:stone", stoneEntry.getCorrespondingBlock());
    }

    @Test
    public void test_itemStackCreate() throws RemoteException {
        ItemStack itemStack = ItemStack.create()
                .material(TestConst.Items.MATERIAL)
                .name(TestConst.Items.NAME);

        log.debug(itemStack);

        assertEquals(1, itemStack.amount());
        assertEquals(itemStack, serviceModel.getItems().named(Materials.DIAMOND_BLOCK, TestConst.Items.NAME));
    }

    @Test
    public void test_createGuiFromType() throws RemoteException {
        Gui gui = serviceModel.createGui(GuiType.CHEST_4_ROW);

        log.debug(gui);

        assertNotNull(gui);
        assertNotNull(gui.getId());
        assertNotNull(gui.getDescription());
        assertNull(gui.getItem(TestConst.Items.SLOT));
    }

    @Test
    public void test_createGuiFromDescription() throws RemoteException {
        Gui gui = serviceModel.createGui(
                GuiDescription.builder()
                        .type(TestConst.Inventory.TYPE)
                        .size(TestConst.Inventory.SIZE)
                        .title(TestConst.Inventory.TITLE)
                        .build());

        log.debug(gui);

        assertNotNull(gui);
        assertNotNull(gui.getId());
        assertNull(gui.getItem(TestConst.Items.SLOT));
    }

    @Test
    public void test_createGuiWithItems() throws RemoteException{
        Gui gui = serviceModel.createGui(GuiType.CHEST);
        gui.setItem(TestConst.Items.SLOT,
                ItemStack.create()
                        .material(TestConst.Items.MATERIAL)
                        .name(TestConst.Items.NAME));

        gui.addClickListener(TestConst.Items.SLOT, log::debug);

        serviceModel.fireClickAction(gui,
                ItemClickEvent.builder()
                        .slot(TestConst.Items.SLOT)
                        .clickType(TestConst.Items.CLICK_TYPE)
                        .itemStack(gui.getItem(TestConst.Items.SLOT))
                        .build());

        log.debug(gui);

        assertNotNull(gui);
        assertNotNull(gui.getId());
        assertNotNull(gui.getDescription());
        assertNotNull(gui.getItem(TestConst.Items.SLOT));
    }
}

