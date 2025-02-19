package com.application.dataService.SpringBootRest.data.service.Impl;

import com.application.dataService.SpringBootRest.data.dto.ProductDTO;
import com.application.dataService.SpringBootRest.data.entities.Product;
import com.application.dataService.SpringBootRest.data.exception.ProductNotFoundException;
import com.application.dataService.SpringBootRest.data.exception.ProductOutOfStockException;
import com.application.dataService.SpringBootRest.data.repository.ProductRepository;
import com.application.dataService.SpringBootRest.data.service.IProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    //Injection Dependence through constructor
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }

    @Override
    public ProductDTO findByIdProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        //Convertir DTO a entiendad
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        //Convertir nuevamente entidad guardada a DTO
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (productDTO.getName() != null){
        product.setName(productDTO.getName());
        }

        if (productDTO.getPrice() != null){
            product.setPrice(productDTO.getPrice());
        }

        if (productDTO.getStock() != null){
            product.setStock(productDTO.getStock());
        }

        Product updateProduct = productRepository.save(product);
        return modelMapper.map(updateProduct, ProductDTO.class);
    }

    @Override
    public void deleteByIdProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO updateStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        if (product.getStock() < quantity) {
            throw new ProductOutOfStockException(
                    String.format("Product %s has insufficient stock. Required: %d, Available: %d",
                            product.getName(), quantity, product.getStock())
            );
        }

        product.setStock(product.getStock() - quantity);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }
}
