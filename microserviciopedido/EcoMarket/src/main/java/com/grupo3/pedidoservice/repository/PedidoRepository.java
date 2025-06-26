package com.grupo3.pedidoservice.repository;

import com.grupo3.pedidoservice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
