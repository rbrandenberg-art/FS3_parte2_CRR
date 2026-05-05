package com.SmartLogix.Pedido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SmartLogix.Pedido.model.EstadoPedido;
import com.SmartLogix.Pedido.model.Pedido;
import com.SmartLogix.Pedido.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional
    public Pedido crear(Pedido pedido) {
        // Calcular total desde los detalles
        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        pedido.setTotal(total);
        pedido.getDetalles().forEach(d -> {
            d.setPedido(pedido);
            d.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
        });
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        });
    }

    public boolean cancelar(Long id) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(EstadoPedido.CANCELADO);
            pedidoRepository.save(pedido);
            return true;
        }).orElse(false);
    }
}
