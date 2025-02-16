package com.application.dataService.SpringBootRest.data.service.Impl;

import com.application.dataService.SpringBootRest.data.entities.Product;
import com.application.dataService.SpringBootRest.data.repository.ProductRepository;
import com.application.dataService.SpringBootRest.data.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    //Injection Dependence through constructor
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findByIdProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existing ->{
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    existing.setStock(product.getStock());
                    return productRepository.save(existing);
                })
                .orElseThrow(()-> new RuntimeException("Product not found"));
    }

    @Override
    public void deleteByIdProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product updateStock(Long id, int quantity) {
        return  productRepository.findById(id)
                .map(existing -> {
                    int newStock = existing.getStock() - quantity;
                    if (newStock < 0){
                        throw new RuntimeException("Insufficient stock for product with ID: " + id);
                    }
                    existing.setStock(newStock);
                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
