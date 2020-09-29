package com.pricebasket.app.service;

import com.pricebasket.app.dal.InMemoryItemDAOImpl;
import com.pricebasket.app.model.Item;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemServiceTest {
    @Test
    public void ShouldReturnItemsIfProductExists() {

        ItemService itemService = new ItemService(new InMemoryItemDAOImpl());
        Item item = itemService.getItem("Soup");
        assertEquals(item.getItemName(), "Soup");
        // assertEquals(item.getPrice(), .65);
    }

    @Test
    public void ShouldReturnNullIfProductNotExists() {

        ItemService itemService = new ItemService(new InMemoryItemDAOImpl());
        Item item = itemService.getItem("Banana");
        assertEquals(item.getItemName(), "Soup");
        // assertEquals(item.getPrice(), .65);
    }

}