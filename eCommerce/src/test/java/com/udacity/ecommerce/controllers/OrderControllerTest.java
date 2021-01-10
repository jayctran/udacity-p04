package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.*;
import com.udacity.ecommerce.model.persistence.repositories.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.udacity.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private Item item;
    private Item item2;
    private List<Item> items = new ArrayList<>();
    private User user;

    @BeforeEach
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
        user = createUser(1, "james", "udacity");
        item = TestUtils.createItem(1, "ball", BigDecimal.valueOf(10.99), "round ball");
        item2 = TestUtils.createItem(2, "bat", BigDecimal.valueOf(15.99), "big ball");
        items.add(item);
        items.add(item2);
    }

    @Test
    public void submitTest() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<UserOrder> submitted = orderController.submit(user.getUsername());
        assertNotNull(submitted);

        User user_submit = submitted.getBody().getUser();
        List<Item> items_submit = submitted.getBody().getItems();
        assertEquals(user, user_submit);
        assertEquals(items, items_submit);
    }

    @Test
    public void getOrdersForUserTest() {
        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(items);
        order.setTotal(BigDecimal.ONE);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Lists.list(order));

        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(ordersForUser);
        assertEquals(1,ordersForUser.getBody().stream().count());
        assertEquals(Lists.list(order),ordersForUser.getBody());
    }

}