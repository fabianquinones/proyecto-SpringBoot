package com.grupo3.pedidoservice.service;

import com.grupo3.pedidoservice.model.Pedido;
import com.grupo3.pedidoservice.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PedidoRepository pedidoRepository;

    public void crearPedido(Long usuarioId, Long productoId, int cantidad) {
        // 1. Verificar si el producto existe y obtener su información
        String productoUrl = "http://localhost:8081/api/productos/" + productoId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "admin"); // Cambia por tu usuario y contraseña

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Obtener el producto
        Map<String, Object> producto;
        try {
            producto = restTemplate.exchange(
                productoUrl,
                org.springframework.http.HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            ).getBody();
            System.out.println("Producto recibido: " + producto); // <-- Agrega esto para debug
        } catch (Exception e) {
            throw new RuntimeException("Producto no encontrado");
        }

        // 2. Verificar stock suficiente
        Object stockObj = producto.get("stock");
        Integer stock = null;
        if (stockObj instanceof Integer) {
            stock = (Integer) stockObj;
        } else if (stockObj instanceof Number) {
            stock = ((Number) stockObj).intValue();
        } else if (stockObj != null) {
            stock = Integer.parseInt(stockObj.toString());
        }

        if (stock == null || stock < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        // 3. Reducir el stock
        String actualizarStockUrl = "http://localhost:8081/api/productos/" + productoId + "/stock";
        Map<String, Object> stockUpdate = new HashMap<>();
        stockUpdate.put("cantidad", cantidad);

        HttpEntity<Map<String, Object>> stockRequest = new HttpEntity<>(stockUpdate, headers);
        restTemplate.postForObject(actualizarStockUrl, stockRequest, Void.class);

        // 4. Crear el pedido
        String url = "http://localhost:8082/api/pedidos";
        Map<String, Object> pedido = new HashMap<>();
        pedido.put("usuarioId", usuarioId);
        pedido.put("productoId", productoId);
        pedido.put("cantidad", cantidad);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(pedido, headers);
        restTemplate.postForObject(url, request, Object.class);
    }

    public Pedido realizarPedido(Pedido pedido) {
        // Puedes agregar lógica extra aquí si lo necesitas
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }
}
