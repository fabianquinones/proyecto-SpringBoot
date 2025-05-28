package com.grupo3.pedidoservice.controller;

import com.grupo3.pedidoservice.model.Pedido;
import com.grupo3.pedidoservice.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> realizarPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.realizarPedido(pedido));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @PostMapping("/crear")
    public ResponseEntity<Void> crearPedido(@RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        Long productoId = Long.valueOf(body.get("productoId").toString());
        int cantidad = Integer.parseInt(body.get("cantidad").toString());
        pedidoService.crearPedido(usuarioId, productoId, cantidad);
        return ResponseEntity.ok().build();
    }
}
