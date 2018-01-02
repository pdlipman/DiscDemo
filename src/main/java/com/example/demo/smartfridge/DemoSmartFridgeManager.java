package com.example.demo.smartfridge;

import java.util.*;
import java.util.stream.Collectors;

public class DemoSmartFridgeManager implements SmartFridgeManager {
    private Map<String, SmartFridgeItem> contents;
    private Set<Long> ignoreItemTypes = new HashSet<>();

    public DemoSmartFridgeManager() {
        this.contents = new HashMap<>();
    }

    public DemoSmartFridgeManager(Map<String, SmartFridgeItem> contents) {
        this.contents = contents;
    }

    @Override
    public void handleItemRemoved(String itemUUID) {
        contents.remove(itemUUID);
    }

    @Override
    public void handleItemAdded(long itemType, String itemUUID, String name, Double fillFactor) {
        StandardSmartFridgeItem item = new StandardSmartFridgeItem(itemType, itemUUID, name, fillFactor);
        contents.put(itemUUID, item);
    }

    private Set<Long> getApprovedItemTypes() {
        return contents
                .values()
                .stream()
                .map(SmartFridgeItem::getItemType)
                .filter(e -> !ignoreItemTypes.contains(e))
                .collect(Collectors.toSet());
    }

    @Override
    public Object[] getItems(Double fillFactor) {

        Set<Long> itemTypes = getApprovedItemTypes();

        Object[] getItems = itemTypes
                .stream()
                .map(itemType -> {
                    Double fill = getFillFactor(itemType);
                    if (fill > fillFactor) {
                        return null;
                    }

                    Object[] returnItem = new Object[]{itemType, fill};
                    return returnItem;
                })
                .filter(Objects::nonNull)
                .toArray();

        return getItems;
    }

    @Override
    public Double getFillFactor(long itemType) {
        OptionalDouble fillFactor = contents
                .values()
                .stream()
                .filter(e -> e.getItemType() == itemType)
                .mapToDouble(SmartFridgeItem::getFillFactor)
                .filter(e -> e > 0.0d)
                .average();
        if (fillFactor.isPresent()) {
            return fillFactor.getAsDouble();
        }
        return 0.0d;
    }

    @Override
    public void forgetItem(long itemType) {
        ignoreItemTypes.add(itemType);
    }

    @Override
    public String toString() {
        String toStringValue = "";

        if (contents.size() > 0) {
            toStringValue = "\n" +
                    contents
                            .values()
                            .stream()
                            .map(e -> e.toString())
                            .collect(Collectors.joining("\n")) +
            "\n";
        }
        return "DemoSmartFridgeManager{" + toStringValue + "}";
    }

    /**
     * for unit testing
     *
     * @return SmartFridge contents
     */
    Map<String, SmartFridgeItem> getContents() {
        return contents;
    }

    /**
     * for unit testing
     *
     * @return SmartFridge ignored item types
     */
    Set<Long> getIgnoreItemTypes() {
        return ignoreItemTypes;
    }
}
