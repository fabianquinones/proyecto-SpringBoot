package com.grupo3.productoservice.service;

import com.grupo3.productoservice.model.Producto;
import com.grupo3.productoservice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Producto crearSiEsAdmin(Producto producto, String username) {
        String sql = "SELECT rol FROM usuario WHERE username = ?";
        try {
            String rol = jdbcTemplate.queryForObject(sql, String.class, username);
            if ("ADMIN".equalsIgnoreCase(rol)) {
                return productoRepository.save(producto);
            } else {
                throw new SecurityException("No tienes permisos para crear productos");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new SecurityException("Usuario no encontrado");
        }
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto actualizar(Long id, Producto p) {
        return productoRepository.findById(id).map(prod -> {
            prod.setNombre(p.getNombre());
            prod.setPrecio(p.getPrecio());
            prod.setStock(p.getStock());
            return productoRepository.save(prod);
        }).orElse(null);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public boolean reducirStock(Long id, int cantidad) {
        Optional<Producto> prodOpt = productoRepository.findById(id);
        if (prodOpt.isPresent()) {
            Producto p = prodOpt.get();
            if (p.getStock() >= cantidad) {
                p.setStock(p.getStock() - cantidad);
                productoRepository.save(p);
                return true;
            }
        }
        return false;
    }
}
