package com.udacity.ecommerce;

import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {
    public static void injectObject(Object target,String fieldName,Object toBeInjected){
        boolean wasPrivate = false;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate=true;
            }
            field.set(target,toBeInjected);
            if (wasPrivate){
                wasPrivate=false;
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static ModifyCartRequest modifyCartRequest(String username, long itemId, int quantity) {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(username);
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(quantity);
        return cartRequest;
    }

    public static Item createItem(long id, String name, BigDecimal price, String description) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        return item;
    }

    public static User createUser(long id, String username, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public static CreateUserRequest createUserRequest(String username, String password, String confirmPassword){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(confirmPassword);
        return createUserRequest;
    }
}
