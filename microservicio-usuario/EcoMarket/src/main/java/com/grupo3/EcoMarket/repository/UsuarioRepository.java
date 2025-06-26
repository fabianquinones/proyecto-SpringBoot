package com.grupo3.EcoMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.EcoMarket.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}
