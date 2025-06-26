package com.grupo3.productoservice.controller;

import com.grupo3.productoservice.model.Producto;
import com.grupo3.productoservice.repository.ProductoRepository;
import com.grupo3.productoservice.service.ProductoService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public ProductoController(ProductoRepository productoRepository, ProductoService productoService) {
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }

    @GetMapping
    public CollectionModel<EntityModel<Producto>> listar() {
        List<EntityModel<Producto>> productos = productoRepository.findAll().stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).listar()).withRel("productos")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listar()).withSelfRel());
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
    public ResponseEntity<EntityModel<Producto>> buscarPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).buscarPorId(id)).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).listar()).withRel("productos")))
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
