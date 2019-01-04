package com.yjs.service.impl;

import com.yjs.dataobject.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceimplTest {

    @Autowired
    private UserServiceimpl userServiceimpl;

    @Test
    public void findByUsernameAndPassword() throws Exception {
        User user = userServiceimpl.findByUsernameAndPassword("admin","123456");
        Assert.assertNotNull(user);
    }

}