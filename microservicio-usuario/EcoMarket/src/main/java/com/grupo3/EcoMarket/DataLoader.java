package com.grupo3.EcoMarket;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.grupo3.EcoMarket.model.Usuario;
import com.grupo3.EcoMarket.repository.UsuarioRepository;

@Component
public class DataLoader {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void cargarDatosFalsos() {
        Faker faker = new Faker();
        String[] roles = {"ADMIN", "USER", "CLIENTE"};

        for (int i = 0; i < 10; i++) {
            Usuario usuario = new Usuario();
            usuario.setUsername(faker.name().username());
            usuario.setPassword(faker.internet().password());
            usuario.setRol(roles[faker.random().nextInt(roles.length)]);
            usuarioRepository.save(usuario);
        }
    }
}
