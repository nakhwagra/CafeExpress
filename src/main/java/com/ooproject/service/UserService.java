package com.ooproject.service;

import com.ooproject.model.User;
import com.ooproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllByRole(String role) {
        return userRepository.findByRole(role);
    }
}