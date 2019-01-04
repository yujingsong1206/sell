package com.yjs.service;


import com.yjs.dataobject.User;

public interface UserService {

    User findByUsernameAndPassword(String username, String password);

}
