package com.example.demo.smartfridge;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class StandardSmartFridgeItemTests {
    private SmartFridgeItem item;
    private String uuid = UUID.randomUUID().toString();

    @Before
    public void beforeEach() {
        item = new StandardSmartFridgeItem(
                1,
                uuid,
                "testItem",
                1.0d
        );
    }

    @Test
    public void toStringShouldBeOverwritten() {
        String expected = "StandardSmartFridgeItem{\n" +
                " itemType=1,\n" +
                " itemUUID='" + uuid + "',\n" +
                " name='testItem',\n" +
                " fillFactor=1.0\n" +
                "}";
        String actual = item.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void getItemType() {
        long expected = 1l;
        assertEquals(expected, item.getItemType());
    }

    @Test
    public void getItemUUID() {
        String expected = uuid;
        assertEquals(expected, item.getItemUUID());
    }

    @Test
    public void getName() {
        String expected = "testItem";
        assertEquals(expected, item.getName());
    }

    @Test
    public void getFillFactor() {
        Double expected = 1.0d;
        assertEquals(expected, item.getFillFactor());
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void setNegativeFillFactor() {
        exception.expect(IllegalArgumentException.class);
        item.setFillFactor(-1.0d);
    }

    @Test
    public void setFillFactorOverOne() {
        exception.expect(IllegalArgumentException.class);
        item.setFillFactor(1.1d);
    }
}
