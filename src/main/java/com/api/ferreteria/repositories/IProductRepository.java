package com.api.ferreteria.repositories;

import com.api.ferreteria.models.ProductModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long> {
}
