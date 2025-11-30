package com.example.pedidoservice.repository;

import com.example.pedidoservice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
