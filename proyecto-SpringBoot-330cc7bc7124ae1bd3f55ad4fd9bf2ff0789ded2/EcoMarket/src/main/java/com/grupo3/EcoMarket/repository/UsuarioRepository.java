package com.grupo3.EcoMarket.repository;

import com.grupo3.EcoMarket.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por correo exacto (útil para login o validaciones)
    Optional<Usuario> findByCorreo(String correo);

    // Buscar usuarios cuyo nombre contenga cierta palabra (insensible a mayúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}
