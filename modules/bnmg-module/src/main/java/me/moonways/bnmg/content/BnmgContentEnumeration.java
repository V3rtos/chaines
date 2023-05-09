package me.moonways.bnmg.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bnmg.component.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BnmgContentEnumeration implements Enumeration<String> {

    private static final int TOTAL_COMPONENT_GROUPS_COUNT = 3;
    private static final int ACTIONS_LINE_INDEX = 8;
    private static final String ACTION_GROUP_NAME_PREFIX = "action";
    private static final String DISPLAY_COMPONENT_PREFIX = "!";
    private static final String ITEM_COMPONENT_PREFIX = "@";


    private final List<String> contentLinesList;
    private int position;

    private String currentGroup;
    private final List<BnmgComponentLine> componentLines = new ArrayList<>();

    private final List<String> actionsList = new ArrayList<>();

    private int componentGroupsFounded;

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

        } else if (isGroup(currentLine)) {
            String groupName = getGroupName(currentLine);

            if (++componentGroupsFounded < TOTAL_COMPONENT_GROUPS_COUNT && groupName.toLowerCase().contains(ACTION_GROUP_NAME_PREFIX))
                throw new IllegalArgumentException("action group must be have low priority");

            currentGroup = groupName;

        } else {
            BnmgComponentLine componentLine = getComponentLine(currentLine);

            if (currentGroup != null && currentGroup.toLowerCase().startsWith(ACTION_GROUP_NAME_PREFIX)) {
                actionsList.add(componentLine.getContent());

            } else {
                if (currentGroup != null) {
                    ItemGroup groupByName = ItemGroup.getGroupByName(currentGroup);

                    if (groupByName == null) {
                        throw new NullPointerException("group");
                    }

                    int internalIndex = groupByName.toInternalIndex(componentLine.getIndex());
                    if (internalIndex < 0) {
                        throw new IllegalArgumentException(componentLine.toString());
                    }

                    componentLine.setIndex(internalIndex);
                }
                componentLines.add(componentLine);
            }
        }

        if (!hasMoreElements()) {
            initLastInitializedComponent();
        }

        return currentLine;
    }

    private void initLastInitializedComponent() {
        BnmgComponent lastComponent = !initializedComponentsList.isEmpty() ? initializedComponentsList.get(initializedComponentsList.size() - 1) : null;
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
