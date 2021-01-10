package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeEach
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCarTest(){
        ModifyCartRequest cartRequest = TestUtils.modifyCartRequest("james", 1l, 1);

        User user = TestUtils.createUser(1,"james","udacity");
        Item item = TestUtils.createItem(1, "ball", BigDecimal.valueOf(10.99), "round ball");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername("james")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponse = cartController.addToCart(cartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest() {

        User user = TestUtils.createUser(1,"james","udacity");
        Item item = TestUtils.createItem(1, "ball", BigDecimal.valueOf(10.99), "round ball");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        user.setCart(cart);

        ModifyCartRequest cartRequest = TestUtils.modifyCartRequest("james", 1L, 2);

        when(userRepository.findByUsername("james")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponse = cartController.addToCart(cartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());

        Cart savedCart = cartResponse.getBody();
        assertNotNull(savedCart);
        assertEquals(cart, savedCart);
        assertEquals(cart.getId(), savedCart.getId());
        assertEquals(cart.getUser(), savedCart.getUser());
        ResponseEntity<Cart> cartRemovedResponse = cartController.removeFromCart(cartRequest);
        assertNotNull(cartRemovedResponse);
        assertEquals(200, cartRemovedResponse.getStatusCodeValue());

        Cart cartRemoved = cartRemovedResponse.getBody();
        assertNotNull(cartRemoved);
        assertEquals(cart, cartRemoved);
        assertEquals(cart.getItems().stream().count(), cartRemoved.getItems().stream().count());
        assertEquals(cart.getUser(), cartRemoved.getUser());

        // Removing item not in cart
        ModifyCartRequest cartRemove = TestUtils.modifyCartRequest("james", 2L, 5);
        ResponseEntity<Cart>   notInCartRemovedResponse = cartController.removeFromCart(cartRemove);
        assertNotNull(notInCartRemovedResponse);

        Cart notInCartRemoved = notInCartRemovedResponse.getBody();
        assertNull(notInCartRemoved);
    }
}