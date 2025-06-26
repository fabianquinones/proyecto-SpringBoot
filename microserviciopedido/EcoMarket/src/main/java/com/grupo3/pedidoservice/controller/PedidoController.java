package com.grupo3.pedidoservice.controller;

import com.grupo3.pedidoservice.model.Pedido;
import com.grupo3.pedidoservice.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    public EntityModel<Pedido> obtenerPedido(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoService.buscarPorId(id);
        Pedido pedido = pedidoOpt.orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return EntityModel.of(pedido,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).obtenerPedido(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).listar()).withRel("pedidos")
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<Pedido>> listar() {
        List<EntityModel<Pedido>> pedidos = pedidoService.listar().stream()
            .map(pedido -> EntityModel.of(pedido,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).obtenerPedido(pedido.getId())).withSelfRel()
            ))
            .toList();

        return CollectionModel.of(pedidos,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).listar()).withSelfRel()
        );
    }
}
