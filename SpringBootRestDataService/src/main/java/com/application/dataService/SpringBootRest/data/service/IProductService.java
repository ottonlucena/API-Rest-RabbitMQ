package com.application.dataService.SpringBootRest.data.service;

import com.application.dataService.SpringBootRest.data.dto.ProductDTO;

import java.util.List;

public interface IProductService {

    List<ProductDTO> findAllProducts();

    ProductDTO findByIdProduct(Long id);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteByIdProduct(Long id);

    ProductDTO updateStock(Long id, int quantity);
}
