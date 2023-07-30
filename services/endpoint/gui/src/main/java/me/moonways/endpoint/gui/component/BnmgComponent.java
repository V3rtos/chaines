package me.moonways.endpoint.gui.component;

public interface BnmgComponent {

    String getName();

    BnmgComponentLine[] getLines();

    void updateLine(BnmgComponentLine bnmgComponentLine);
}
