package me.moonways.bnmg.test;

import me.moonways.bnmg.component.BnmgComponent;
import me.moonways.bnmg.component.BnmgComponentLine;
import me.moonways.bnmg.content.BnmgContentEnumeration;

import java.util.Arrays;
import java.util.List;

public class EnumerationTest {

    private static final String TEST_BNMG_FILE_CONTENT =
            "!main:\n" +
            "    @1 Mode Selector\n" +
            "    @3 6\n" +
            "@hub:\n" +
            "    item:\n" +
            "        #1 slot 1\n" +
            "        #2 stone\n" +
            "        #3 1\n" +
            "        #4 1\n" +
            "    display:\n" +
            "        #1 gui_hub_name\n" +
            "        #2 gui_hub_lore\n" +
            "    action:\n" +
            "        #1 serv hub\n" +
            "        #2 msg &eTeleporting to Hub...\n" +
            "@skywars:\n" +
            "    item:\n" +
            "        #1 matrix 1,2\n" +
            "        #2 ender_pearl\n" +
            "        #3 1\n" +
            "        #4 1\n" +
            "    display:\n" +
            "        #1 sw_hub_name\n" +
            "        #2 sw_hub_lore\n" +
            "    action:\n" +
            "        #1 serv swlobby\n" +
            "        #2 msg &eTeleporting to SkyWars...";

    public static void main(String[] args) {
        BnmgContentEnumeration bnmgContentEnumeration = new BnmgContentEnumeration(
                Arrays.asList(TEST_BNMG_FILE_CONTENT.split("\n")));

        while (bnmgContentEnumeration.hasMoreElements()) {
            bnmgContentEnumeration.nextElement();
        }

        List<BnmgComponent> initializedComponentsList = bnmgContentEnumeration.getInitializedComponentsList();

        for (BnmgComponent component : initializedComponentsList) {
            System.out.println("component: " + component);
        }
    }
}
