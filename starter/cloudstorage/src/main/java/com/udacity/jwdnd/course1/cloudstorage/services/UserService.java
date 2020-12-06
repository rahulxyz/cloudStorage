package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private UserMapper userMapper;
    private EncryptionService encryptionService;

    public UserService(UserMapper userMapper, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public Boolean isUsernameAvailable(String username){
        return userMapper.getUser(username) == null;
    }

    public User getUser(String username){
        return userMapper.getUser(username);
    }

    public int createUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        String encryptedPassword = encryptionService.encryptValue(user.getPassword(), encodedSalt);

        return userMapper.insert(new User(null, user.getUsername(), encodedSalt, encryptedPassword, user.getFirstName(), user.getLastName()));
    }
}
