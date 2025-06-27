package com.api.ferreteria.controllers;

import com.api.ferreteria.models.ProductModel;
import com.api.ferreteria.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    @CrossOrigin("*")
    public ArrayList<ProductModel> getProducts() {
        return this.productService.getProducts();
    }

    // Create a product
    @PostMapping
    @CrossOrigin("*")
    public ProductModel saveProduct(@RequestBody ProductModel product) {
        return this.productService.saveProduct(product);
    }

    // Get a product by id
    @GetMapping(path = "/{id}")
    @CrossOrigin("*")
    public Optional<ProductModel> getProductById(@PathVariable Long id) {
        return this.productService.getById(id);
    }

    // Update a product by id
    @PutMapping(path = "/{id}")
    @CrossOrigin("*")
    public ProductModel updateProductById(@RequestBody ProductModel request, @PathVariable Long id) {
        return this.productService.updateById(request, id);
    }

    // Delete a product by id
    @DeleteMapping(path = "/{id}")
    @CrossOrigin("*")
    public String deleteProductById(@PathVariable Long id) {
        Boolean ok = this.productService.deleteProduct(id);

        if (ok) {
            return "Product with id " + id + " deleted";
        } else {
            return "Error";
        }
    }

    // Create a ton of products at the same time coming from a JSON format list (Postman only at the moment)
    @PostMapping("/bulk")
    @CrossOrigin("*")
    public List<ProductModel> saveMultipleProducts(@RequestBody List<ProductModel> products) {
        return this.productService.saveProducts(products);
    }
}