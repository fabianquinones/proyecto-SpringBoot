package com.grupo3.productoservice.repository;

import com.grupo3.productoservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
