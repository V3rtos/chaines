package me.moonways.bnmg.content;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.service.bnmg.BnmgFile;
import me.moonways.bnmg.component.BnmgComponent;
import me.moonways.bridgenet.service.bnmg.descriptor.GuiDescriptor;

import java.util.List;

@RequiredArgsConstructor
public class BnmgContentConstructor {

    private final BnmgFile bnmgFile;

    private GuiDescriptor descriptor;

    public void construct() {
        BnmgContentEnumeration enumeration = new BnmgContentEnumeration(bnmgFile.toContentLinesList());
        enumeration.iterateEnumerationElements();

        List<BnmgComponent> initializedBnmgComponentsList =
                enumeration.getInitializedComponentsList();
    }
}
