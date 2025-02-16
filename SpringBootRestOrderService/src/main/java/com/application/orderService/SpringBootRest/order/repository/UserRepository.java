package com.application.orderService.SpringBootRest.order.repository;

import com.application.orderService.SpringBootRest.order.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
}
