package com.application.dataService.SpringBootRest.data.controllers;

import com.application.dataService.SpringBootRest.data.entities.Product;
import com.application.dataService.SpringBootRest.data.service.Impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> productList = productService.findAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return productService.findByIdProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product){
        Product createProduct = productService.createProduct(product);
        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        productService.deleteByIdProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestParam int quantity){
        try {
            Product updated = productService.updateStock(id, quantity);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
