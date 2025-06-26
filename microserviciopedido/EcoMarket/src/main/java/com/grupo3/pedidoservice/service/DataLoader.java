package com.grupo3.pedidoservice.service;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.grupo3.pedidoservice.model.Pedido;
import com.grupo3.pedidoservice.repository.PedidoRepository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostConstruct
    public void loadFakePedidos() {
        Faker faker = new Faker();
        List<Pedido> pedidos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Pedido pedido = new Pedido();
            pedido.setUsuarioId(faker.number().numberBetween(1L, 100L));    // IDs de usuario aleatorios
            pedido.setProductoId(faker.number().numberBetween(1L, 50L));    // IDs de producto aleatorios
            pedido.setCantidad(faker.number().numberBetween(1, 10));        // Cantidad aleatoria entre 1 y 10
            pedidos.add(pedido);
        }

        pedidoRepository.saveAll(pedidos);
        System.out.println("Pedidos falsos generados y guardados: " + pedidos.size());
    }
}
