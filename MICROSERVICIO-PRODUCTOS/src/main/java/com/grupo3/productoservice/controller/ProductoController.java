package com.grupo3.productoservice.controller;

import com.grupo3.productoservice.model.Producto;
import com.grupo3.productoservice.repository.ProductoRepository;
import com.grupo3.productoservice.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos") // Cambia aquí la ruta base
public class ProductoController {
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public ProductoController(ProductoRepository productoRepository, ProductoService productoService) {
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto, @RequestParam String username) {
        try {
            Producto nuevo = productoService.crearSiEsAdmin(producto, username);
            return ResponseEntity.ok(nuevo);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<?> reducirStock(@PathVariable Long id, @RequestBody(required = true) java.util.Map<String, Object> body) {
        int cantidad = Integer.parseInt(body.get("cantidad").toString());
        boolean actualizado = productoService.reducirStock(id, cantidad);
        if (actualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Stock insuficiente o producto no encontrado");
        }
    }
}
