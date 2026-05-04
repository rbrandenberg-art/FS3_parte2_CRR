package com.SmartLogix.Pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SmartLogix.Pedido.model.EstadoPedido;
import com.SmartLogix.Pedido.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByEstado(EstadoPedido estado);
}

