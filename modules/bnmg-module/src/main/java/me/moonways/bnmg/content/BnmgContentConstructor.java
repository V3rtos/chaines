package me.moonways.bnmg.content;

import lombok.RequiredArgsConstructor;
import me.moonways.bnmg.component.ItemComponent;
import me.moonways.bridgenet.service.bnmg.BnmgFile;
import me.moonways.bnmg.component.BnmgComponent;
import me.moonways.bridgenet.service.bnmg.descriptor.GuiDescriptor;
import me.moonways.bridgenet.service.bnmg.descriptor.ItemDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class BnmgContentConstructor {

    private final BnmgFile bnmgFile;

    public GuiDescriptor constructDescriptor() {
        BnmgContentEnumeration enumeration = new BnmgContentEnumeration(bnmgFile.toContentLinesList());
        enumeration.iterateEnumerationElements();

        List<BnmgComponent> initializedBnmgComponentsList =
                enumeration.getInitializedComponentsList();

        ItemDescriptor[] itemDescriptorsArray = toItemDescriptorsArray(initializedBnmgComponentsList);
        throw new UnsupportedOperationException();
    }

    private ItemDescriptor[] toItemDescriptorsArray(List<BnmgComponent> initializedBnmgComponentsList) {
        Set<ItemDescriptor> descriptorSet = new HashSet<>();

        for (BnmgComponent component : initializedBnmgComponentsList) {

            if (component instanceof ItemComponent) {
                ItemComponent itemComponent = (ItemComponent) component;
                descriptorSet.add(toDescriptor(itemComponent));
            }
        }

        return descriptorSet.toArray(new ItemDescriptor[0]);
    }

    private ItemDescriptor toDescriptor(ItemComponent itemComponent) {
        throw new UnsupportedOperationException();
    }
}
