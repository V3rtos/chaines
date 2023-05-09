package me.moonways.bnmg.component;

public interface BnmgComponent {

    String getName();

    BnmgComponentLine[] getLines();

    void updateLine(BnmgComponentLine bnmgComponentLine);
}
