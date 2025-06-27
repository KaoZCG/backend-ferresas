package com.api.ferreteria.services;

import com.api.ferreteria.models.ProductModel;
import com.api.ferreteria.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // Inyección de dependencias
    @Autowired
    IProductRepository productRepository;

    // Get all products
    public ArrayList<ProductModel> getProducts(){
        return (ArrayList<ProductModel>) productRepository.findAll();
    }

    // Create a product
    public ProductModel saveProduct(ProductModel product){
        return productRepository.save(product);
    }

    // Get a product by id
    public Optional<ProductModel> getById(Long id){
        return productRepository.findById(id);
    }

    // Update a product by id
    public ProductModel updateById(ProductModel request, Long id) {
        ProductModel product = productRepository.findById(id).get();

        product.setName(request.getName());
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setAvailable(request.getAvailable());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());

        return productRepository.save(product);
    }

    // Delete a product by id
    public Boolean deleteProduct(Long id){
        try {
            productRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    // Create products with a JSON format list POST
    public List<ProductModel> saveProducts(List<ProductModel> products){
        return productRepository.saveAll(products);
    }
}
