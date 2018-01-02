package com.example.demo.smartfridge;

public interface SmartFridgeItem {
    long getItemType();

    String getItemUUID();

    String getName();

    Double getFillFactor();
    void setFillFactor(Double fillFactor);
}
