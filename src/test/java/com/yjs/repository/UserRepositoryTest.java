package com.yjs.repository;

import com.yjs.dataobject.User;
import com.yjs.utils.KeyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by sjyjs on 2019/1/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save(){
        User user = new User();
        user.setUserId(KeyUtil.genUniqueKey());
        user.setUsername("admin");
        user.setPassword("123456");
        userRepository.save(user);
    }

    @Test
    public void findByUsername() throws Exception {
        User user = userRepository.findByUsername("admin");
        Assert.assertNotNull(user);
    }

    @Test
    public void findByUsernameAndPassword() throws Exception {
        User user = userRepository.findByUsernameAndPassword("admin", "123456");
        Assert.assertNotNull(user);
    }

}