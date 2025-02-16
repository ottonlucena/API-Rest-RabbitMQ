package com.application.orderService.SpringBootRest.order.controller;

import com.application.orderService.SpringBootRest.order.entities.User;
import com.application.orderService.SpringBootRest.order.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User createUser = userService.userCreate(user);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> userList = userService.findAllUser();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return userService.findByIdUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        userService.deleteByIdUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User user){
        try {
            User updated = userService.updateByIdUser(id, user);
            return ResponseEntity.ok(updated);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
