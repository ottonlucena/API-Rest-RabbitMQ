package com.application.dataService.SpringBootRest.data.repository;

import com.application.dataService.SpringBootRest.data.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
