package com.grupo3.productoservice;

import com.grupo3.productoservice.model.Producto;
import com.grupo3.productoservice.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final ProductoRepository productoRepository;

    public DataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @PostConstruct
    public void cargarProductosFalsos() {
        System.out.println("Cargando productos falsos...");
        productoRepository.deleteAll();
        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Producto producto = new Producto();
            producto.setNombre(faker.commerce().productName());
            producto.setPrecio(Double.parseDouble(faker.commerce().price(10.0, 1000.0)));
            producto.setStock(faker.number().numberBetween(1, 100));
            productoRepository.save(producto);
            System.out.println("Producto creado: " + producto.getNombre());
        }
    }
}

