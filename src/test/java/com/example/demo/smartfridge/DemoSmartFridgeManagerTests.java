package com.example.demo.smartfridge;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DemoSmartFridgeManagerTests {
    private Map<String, SmartFridgeItem> contents;

    private String randomUUID() {
        return UUID.randomUUID().toString();
    }

    @Before
    public void beforeEach() {
        contents = new HashMap<>();
    }

    @Test
    public void toStringShouldBeOverwritten() {
        SmartFridgeManager manager = new DemoSmartFridgeManager();

        String expected = "DemoSmartFridgeManager{}";
        String actual = manager.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void toStringWithContents() {
        String uuid1 = randomUUID();
        String uuid2 = randomUUID();

        SmartFridgeManager manager = new DemoSmartFridgeManager();
        manager.handleItemAdded(
                1,
                uuid1,
                "testItem1",
                0.5d
        );
        String expected = "DemoSmartFridgeManager{\n" +
                "StandardSmartFridgeItem{\n" +
                " itemType=1,\n" +
                " itemUUID='" + uuid1 + "',\n" +
                " name='testItem1',\n" +
                " fillFactor=0.5\n" +
                "}\n" +
                "}";
        String actual = manager.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void emptyContentsNotNull() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();
        Map<String, SmartFridgeItem> actualContents = manager.getContents();

        assertNotNull(actualContents);
        assertEquals(0, actualContents.size());
    }

    @Test
    public void contentsNotNull() {
        String uuid = randomUUID();
        contents.put(
                uuid,
                new StandardSmartFridgeItem(
                        1,
                        uuid,
                        "testItem",
                        1.0d
                )
        );
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager(contents);
        Map<String, SmartFridgeItem> actual = manager.getContents();

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void handleItemRemovedEmptyItems() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemRemoved("testUUID");

        Map<String, SmartFridgeItem> actual = manager.getContents();
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void handleItemRemovedSingleItem() {
        String uuid = randomUUID();
        contents.put(
                uuid,
                new StandardSmartFridgeItem(
                        1,
                        uuid,
                        "testItem",
                        1.0d
                )
        );
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager(contents);

        manager.handleItemRemoved(uuid);

        Map<String, SmartFridgeItem> actual = manager.getContents();
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void handleItemRemovedMultipleItem() {
        String uuid1 = randomUUID();
        String uuid2 = randomUUID();
        contents.put(
                uuid1,
                new StandardSmartFridgeItem(
                        1,
                        uuid1,
                        "testItem",
                        1.0d
                )
        );
        contents.put(
                uuid2,
                new StandardSmartFridgeItem(
                        2,
                        uuid2,
                        "testItem2",
                        1.0d
                )
        );
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager(contents);

        Map<String, SmartFridgeItem> actual = manager.getContents();
        assertEquals(2, actual.size());

        manager.handleItemRemoved(uuid1);
        assertEquals(1, actual.size());

        manager.handleItemRemoved(uuid2);
        assertEquals(0, actual.size());
    }

    @Test
    public void handleItemAddedEmptyItems() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem",
                0.5d
        );

        assertEquals(1, manager.getContents().size());
    }

    @Test
    public void handleItemAddedSingleExistingItem() {
        String uuid = randomUUID();
        contents.put(
                uuid,
                new StandardSmartFridgeItem(
                        1,
                        uuid,
                        "testItem",
                        1.0d
                )
        );

        DemoSmartFridgeManager manager = new DemoSmartFridgeManager(contents);
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testNewItem",
                0.5d
        );

        assertEquals(2, manager.getContents().size());
    }

    @Test
    public void handleItemAddedMultipleExistingItems() {
        String uuid1 = randomUUID();
        String uuid2 = randomUUID();
        contents.put(
                uuid1,
                new StandardSmartFridgeItem(
                        1,
                        uuid1,
                        "testItem1",
                        1.0d
                )
        );
        contents.put(
                uuid2,
                new StandardSmartFridgeItem(
                        2,
                        uuid2,
                        "testItem2",
                        1.0d
                )
        );

        DemoSmartFridgeManager manager = new DemoSmartFridgeManager(contents);
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testNewItem",
                0.5d
        );

        assertEquals(3, manager.getContents().size());
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void handleItemAddedNegativeFillFactor() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        exception.expect(IllegalArgumentException.class);
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testNewItem",
                -1.0d
        );
    }

    @Test
    public void handleItemAddedFillFactorOverOne() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        exception.expect(IllegalArgumentException.class);
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testNewItem",
                1.1d
        );
    }

    @Test
    public void getFillFactor() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1",
                0.5d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem2",
                1.0d
        );

        Double expected = 0.75d;
        Double actual = manager.getFillFactor(1);
        assertEquals(expected, actual);
    }


    @Test
    public void getFillFactorIgnoreEmptyContainer() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1",
                0.5d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem2",
                1.0d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem3",
                0.0d
        );

        Double expected = 0.75d;
        Double actual = manager.getFillFactor(1);
        assertEquals(expected, actual);
    }

    @Test
    public void getFillFactorEmptyContainers() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1",
                0.0d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem2",
                0.0d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem3",
                0.0d
        );

        Double expected = 0.0d;
        Double actual = manager.getFillFactor(1);
        assertEquals(expected, actual);
    }


    @Test
    public void getFillFactorMultipleItemTypes() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.1",
                0.5d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.2",
                1.0d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.3",
                0.0d
        );

        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.1",
                0.25d
        );
        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.2",
                0.25d
        );
        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.3",
                0.0d
        );

        manager.handleItemAdded(
                3,
                randomUUID(),
                "testItem3.1",
                0.0d
        );
        Double expected = 0.75d;
        Double actual = manager.getFillFactor(1);
        assertEquals(expected, actual);

        expected = 0.25d;
        actual = manager.getFillFactor(2);
        assertEquals(expected, actual);

        expected = 0.0d;
        actual = manager.getFillFactor(3);
        assertEquals(expected, actual);
    }

    @Test
    public void forgetItems() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.forgetItem(1);
        assertEquals(1, manager.getIgnoreItemTypes().size());

        manager.forgetItem(1);
        assertEquals(1, manager.getIgnoreItemTypes().size());

        manager.forgetItem(2);
        assertEquals(2, manager.getIgnoreItemTypes().size());
    }

    @Test
    public void getItems() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.1",
                0.5d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.2",
                1.0d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.3",
                0.0d
        );

        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.1",
                0.25d
        );
        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.2",
                0.25d
        );
        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.3",
                0.0d
        );

        manager.handleItemAdded(
                3,
                randomUUID(),
                "testItem3.1",
                0.0d
        );

        Object[] actual = manager.getItems(0.75d);
        assertEquals(3, actual.length);

        actual = manager.getItems(0.25d);
        assertEquals(2, actual.length);

        actual = manager.getItems(0.24d);
        assertEquals(1, actual.length);
    }

    @Test
    public void getItemsEmptyItems() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        Object[] actual = manager.getItems(0.75d);
        assertEquals(0, actual.length);
    }

    @Test
    public void getItemsIgnoreItems() {
        DemoSmartFridgeManager manager = new DemoSmartFridgeManager();

        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.1",
                0.5d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.2",
                1.0d
        );
        manager.handleItemAdded(
                1,
                randomUUID(),
                "testItem1.3",
                0.0d
        );

        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.1",
                0.25d
        );
        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.2",
                0.25d
        );
        manager.handleItemAdded(
                2,
                randomUUID(),
                "testItem2.3",
                0.0d
        );

        manager.handleItemAdded(
                3,
                randomUUID(),
                "testItem3.1",
                0.0d
        );

        Object[] actual = manager.getItems(0.75d);
        assertEquals(3, actual.length);

        manager.forgetItem(1);

        actual = manager.getItems(0.75d);
        assertEquals(2, actual.length);

        manager.forgetItem(2);

        actual = manager.getItems(0.75d);
        assertEquals(1, actual.length);

        manager.forgetItem(3);

        actual = manager.getItems(0.75d);
        assertEquals(0, actual.length);

    }
}
