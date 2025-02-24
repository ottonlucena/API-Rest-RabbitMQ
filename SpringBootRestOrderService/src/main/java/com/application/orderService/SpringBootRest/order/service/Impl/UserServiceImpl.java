package com.application.orderService.SpringBootRest.order.service.Impl;

import com.application.orderService.SpringBootRest.order.entities.User;
import com.application.orderService.SpringBootRest.order.exception.UserNotFoundException;
import com.application.orderService.SpringBootRest.order.repository.UserRepository;
import com.application.orderService.SpringBootRest.order.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User userCreate(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByIdUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteByIdUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateByIdUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found"));

    }
}
