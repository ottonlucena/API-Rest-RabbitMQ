package com.application.orderService.SpringBootRest.order.service;

import com.application.orderService.SpringBootRest.order.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User userCreate(User user);

    List<User> findAllUser();

    Optional<User> findByIdUser(Long id);

    void deleteByIdUser(Long id);

    User updateByIdUser(Long id, User user);
}
