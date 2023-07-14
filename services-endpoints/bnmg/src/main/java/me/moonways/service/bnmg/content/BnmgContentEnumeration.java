package me.moonways.service.bnmg.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.bnmg.component.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BnmgContentEnumeration implements Enumeration<String> {

    private static final int TOTAL_COMPONENT_GROUPS_COUNT = 3;
    private static final int ACTIONS_LINE_INDEX = 8;
    private static final String DISPLAY_COMPONENT_PREFIX = "!";
    private static final String ITEM_COMPONENT_PREFIX = "@";


    private final List<String> contentLinesList;
    private int position;

    private int componentGroupsFounded;

    private ItemGroup currentGroup;

    private final List<String> actionsList = new ArrayList<>();

    private final List<BnmgComponentLine> componentLines = new ArrayList<>();

    @Getter
    private final List<BnmgComponent> initializedComponentsList = new ArrayList<>();

    @Override
    public boolean hasMoreElements() {
        return position < contentLinesList.size();
    }

    @Override
    public String nextElement() {
        String currentLine = contentLinesList.get(position++);

        if (isComponent(currentLine)) {
            initializeNewComponent(currentLine);

        } else if (isGroup(currentLine)) {
            initializeNewGroup(currentLine);

        } else {
            initializeNewComponentLine(currentLine);
        }

        if (!hasMoreElements()) {
            initLastInitializedComponent();
        }

        return currentLine;
    }

    public void iterateEnumerationElements() {
        while (hasMoreElements())
            nextElement();
    }

    private void initializeNewComponent(String currentLine) {
        String componentName = getComponentName(currentLine);
        BnmgComponent component;

        if (isComponentDisplay(currentLine)) {
            component = new DisplayComponent(componentName);
        }
        else {
            component = new ItemComponent(componentName);
        }

        initLastInitializedComponent();

        componentLines.clear();
        currentGroup = null;

        initializedComponentsList.add(component);
    }

    private void initializeNewGroup(String currentLine) {
        String groupName = getGroupName(currentLine);
        ItemGroup group = ItemGroup.getGroupByName(groupName);

        if (++componentGroupsFounded < TOTAL_COMPONENT_GROUPS_COUNT && group == ItemGroup.ACTIONS)
            throw new IllegalArgumentException("action group must be have low priority");

        currentGroup = group;
        if (currentGroup == null)
            throw new NullPointerException("group");
    }

    private void initLastInitializedComponent() {
        BnmgComponent lastComponent = !initializedComponentsList.isEmpty()
                ? initializedComponentsList.get(initializedComponentsList.size() - 1) : null;
        if (lastComponent != null) {

            for (BnmgComponentLine componentLine : componentLines)
                lastComponent.updateLine(componentLine);

            if (!actionsList.isEmpty()) {
                String parsedCollection = actionsList.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("; "));

                BnmgComponentLine actionsLine = new BnmgComponentLine(ACTIONS_LINE_INDEX, String.format("[%s]", parsedCollection));
                lastComponent.updateLine(actionsLine);
            }
        }
    }

    private void initializeNewComponentLine(String currentLine) {
        BnmgComponentLine componentLine = getComponentLine(currentLine);

        if (currentGroup != null) {
            if (currentGroup == ItemGroup.ACTIONS) {
                actionsList.add(componentLine.getContent());
                return;
            }

            int internalIndex = currentGroup.toInternalIndex(componentLine.getIndex());
            if (internalIndex < 0)
                throw new IllegalArgumentException(componentLine.toString());

            componentLine.setIndex(internalIndex);
        }
        componentLines.add(componentLine);
    }

    private void validateCurrentElement(String currentLine) {
        if (currentLine == null) {
            throw new NoSuchElementException("current element line");
        }
    }

    private boolean isComponent(String currentLine) {
        validateCurrentElement(currentLine);
        return (currentLine.startsWith(DISPLAY_COMPONENT_PREFIX) || currentLine.startsWith(ITEM_COMPONENT_PREFIX))
                && currentLine.endsWith(":");
    }

    private boolean isGroup(String currentLine) {
        validateCurrentElement(currentLine);
        return currentLine.startsWith(" ") && currentLine.endsWith(":");
    }

    private String getComponentName(String currentLine) {
        validateCurrentElement(currentLine);
        return currentLine.substring(1, currentLine.length() - 1);
    }

    private boolean isComponentDisplay(String currentLine) {
        validateCurrentElement(currentLine);
        return currentLine.startsWith(DISPLAY_COMPONENT_PREFIX);
    }

    private String getGroupName(String currentLine) {
        validateCurrentElement(currentLine);

        String trim = currentLine.trim();
        return trim.substring(0, trim.length() - 1);
    }

    private BnmgComponentLine getComponentLine(String currentLine) {
        validateCurrentElement(currentLine);

        String line = currentLine.trim().substring(1);
        char indexNumberChar = line.charAt(0);

        if (!Character.isDigit(indexNumberChar)) {
            throw new UnsupportedOperationException("incorrectly line format: " + currentLine.trim());
        }

        int indexNumb = Integer.parseInt(Character.toString(indexNumberChar));
        String contentValue = line.substring(2);

        return new BnmgComponentLine(indexNumb, contentValue);
    }
}
