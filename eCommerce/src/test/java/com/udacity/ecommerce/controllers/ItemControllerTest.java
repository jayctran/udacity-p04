package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private Item item;
    private Item item2;
    private List<Item> items = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
        item = TestUtils.createItem(1, "ball", BigDecimal.valueOf(10.99), "round ball");
        item2 = TestUtils.createItem(2, "bat", BigDecimal.valueOf(15.99), "big ball");
        items.add(item);
        items.add(item2);
    }

    @Test
    public void getItemByIdTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Item savedItem = response.getBody();
        assertNotNull(savedItem);
        assertEquals(item.getName(),savedItem.getName());
    }
    @Test
    public void getItemsByNameTest() {
        when(itemRepository.findByName("name")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("name");
        List<Item> savedItems = response.getBody();
        assertNotNull(savedItems);
        assertEquals(items,savedItems);
    }

}