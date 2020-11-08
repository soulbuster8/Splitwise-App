package com.splitwise.service;

import com.splitwise.model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceImplTest {

    static UserService userService;

    @BeforeClass
    public static void setUp()
    {
        userService = new UserServiceImpl();
    }

    @Test
    public void createUserTest()
    {
        User user = userService.createUser("1", "Aman");
        assertNotNull(user);
        assertEquals("1", user.getUserId());
        assertEquals("Aman", user.getName());
    }

    @Test
    public void createUserTest2()
    {
        System.out.println("hi2");
    }

}