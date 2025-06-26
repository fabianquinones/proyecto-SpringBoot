package com.grupo3.EcoMarket.controller;

import com.grupo3.EcoMarket.model.Usuario;
import com.grupo3.EcoMarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener usuario por ID con HATEOAS
    @GetMapping("/{id}")
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return EntityModel.of(new Usuario()); // O manejar error apropiadamente
        }
        EntityModel<Usuario> recurso = EntityModel.of(usuario);
        recurso.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).getUsuarioById(id)).withSelfRel());
        recurso.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"));
        return recurso;
    }

    // Listar todos los usuarios con HATEOAS
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioRepository.findAll().stream()
                .map(usuario -> EntityModel.of(usuario,
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(UsuarioController.class).getUsuarioById(usuario.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());
    }

    // Crear usuario
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}
