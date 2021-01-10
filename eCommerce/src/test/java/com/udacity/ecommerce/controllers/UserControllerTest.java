package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.*;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.udacity.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private User user;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
        user = TestUtils.createUser(1, "james", "udacity");
    }

    @Test
    public void createUserPasswordTest() throws Exception {
        when(encoder.encode("udacity")).thenReturn("testPass");
        CreateUserRequest createUserRequest = createUserRequest("james","udacity","udacity");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User testUser = response.getBody();
        assertNotNull(testUser);
        assertEquals("james", testUser.getUsername());
        assertEquals("testPass", testUser.getPassword());
    }


    @Test
    public void getUserByIdTest(){

        when(encoder.encode("udacity")).thenReturn("udacity_hashed");

        CreateUserRequest createUserRequest = createUserRequest("james","udacity","udacity");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals("james", user.getUsername());
        assertEquals("udacity_hashed", user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<User> responseUser = userController.findById(user.getId());
        assertNotNull(responseUser);
        assertEquals(200, responseUser.getStatusCodeValue());

        User testUser = responseUser.getBody();
        assertNotNull(testUser);
        assertEquals("james", testUser.getUsername());
        assertEquals("udacity_hashed", testUser.getPassword());

    }

    @Test
    public void getUserByName(){

        when(encoder.encode("udacity")).thenReturn("udacity_hashed");

        CreateUserRequest createUserRequest = createUserRequest("james","udacity","udacity");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals("james", user.getUsername());
        assertEquals("udacity_hashed", user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> responseUser = userController.findByUserName(user.getUsername());
        assertNotNull(responseUser);
        assertEquals(200, responseUser.getStatusCodeValue());

        User testUser = responseUser.getBody();

        assertNotNull(testUser);
        assertEquals("james", testUser.getUsername());
        assertEquals("udacity_hashed", testUser.getPassword());
    }

}