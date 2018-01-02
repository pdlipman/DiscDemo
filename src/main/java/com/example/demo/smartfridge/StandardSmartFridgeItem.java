package com.example.demo.smartfridge;

class StandardSmartFridgeItem implements SmartFridgeItem {
    private long itemType;
    private String itemUUID;
    private String name;
    private Double fillFactor;

    public StandardSmartFridgeItem(long itemType, String itemUUID, String name, Double fillFactor) {
        this.itemType = itemType;
        this.itemUUID = itemUUID;
        this.name = name;

        // using this to throw exception if fillFactor is set less than 0
        setFillFactor(fillFactor);
    }


    public long getItemType() {
        return itemType;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public String getName() {
        return name;
    }

    public Double getFillFactor() {
        return fillFactor;
    }

    /**
     * Based on the SmartFridgeManager.java interface, fillFactor is based on a percentage
     * @param fillFactor the amount of the item that is contained in the SmartFridge container
     *
     * @throws IllegalArgumentException if the fillFactor is less than 0
     *      or greater than 1
     */
    public void setFillFactor(Double fillFactor) {
        if (fillFactor < 0.0d || fillFactor > 1.0d) {
            throw new IllegalArgumentException(
                    "StandardSmartFridgeItem.setFillFactor: Double fillFactor must be >= 0.0 or <= 1.0"
            );
        }
        this.fillFactor = fillFactor;
    }

    @Override
    public String toString() {
        return "StandardSmartFridgeItem{" +
                "\n itemType=" + itemType +
                ",\n itemUUID='" + itemUUID + '\'' +
                ",\n name='" + name + '\'' +
                ",\n fillFactor=" + fillFactor +
                "\n}";
    }
}
