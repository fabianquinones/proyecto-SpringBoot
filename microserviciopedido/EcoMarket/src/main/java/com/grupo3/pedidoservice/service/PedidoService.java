package com.grupo3.pedidoservice.service;

import com.grupo3.pedidoservice.model.Pedido;
import com.grupo3.pedidoservice.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido crearPedido(Long usuarioId, Long productoId, int cantidad) {
        if (usuarioId == null || productoId == null || cantidad <= 0) {
            throw new IllegalArgumentException("Parámetros inválidos para crear el pedido");
        }

        String productoUrl = "http://localhost:8081/api/productos/" + productoId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Map<String, Object> producto;
        try {
            producto = restTemplate.exchange(
                productoUrl,
                org.springframework.http.HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            ).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Producto no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el producto");
        }

        Object stockObj = producto.get("stock");
        Integer stock = null;
        if (stockObj instanceof Integer) {
            stock = (Integer) stockObj;
        } else if (stockObj instanceof Number) {
            stock = ((Number) stockObj).intValue();
        } else if (stockObj != null) {
            try {
                stock = Integer.parseInt(stockObj.toString());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Stock del producto inválido");
            }
        }

        if (stock == null || stock < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        String actualizarStockUrl = "http://localhost:8081/api/productos/" + productoId + "/stock";
        Map<String, Object> stockUpdate = new HashMap<>();
        stockUpdate.put("cantidad", cantidad);

        HttpEntity<Map<String, Object>> stockRequest = new HttpEntity<>(stockUpdate, headers);
        try {
            restTemplate.postForObject(actualizarStockUrl, stockRequest, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el stock del producto");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setProductoId(productoId);
        pedido.setCantidad(cantidad);
        return pedidoRepository.save(pedido);
    }

    public Pedido realizarPedido(Pedido pedido) {
        if (pedido == null || pedido.getUsuarioId() == null || pedido.getProductoId() == null || pedido.getCantidad() == null || pedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("Datos de pedido inválidos");
        }
        // Aquí podrías reutilizar la lógica de crearPedido si lo deseas
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
}
