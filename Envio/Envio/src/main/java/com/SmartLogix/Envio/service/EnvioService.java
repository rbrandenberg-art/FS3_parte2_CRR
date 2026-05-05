package com.SmartLogix.Envio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.SmartLogix.Envio.model.Envio;
import com.SmartLogix.Envio.model.EstadoEnvio;
import com.SmartLogix.Envio.repository.EnvioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;

    public List<Envio> obtenerTodos() {
        return envioRepository.findAll();
    }

    public Optional<Envio> obtenerPorId(Long id) {
        return envioRepository.findById(id);
    }

    public Optional<Envio> obtenerPorPedido(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId);
    }

    public Optional<Envio> rastrear(String numeroSeguimiento) {
        return envioRepository.findByNumeroSeguimiento(numeroSeguimiento);
    }

    public Envio crear(Envio envio) {
        envio.setNumeroSeguimiento(generarNumeroSeguimiento());
        envio.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(5));
        return envioRepository.save(envio);
    }

    public Optional<Envio> actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        return envioRepository.findById(id).map(envio -> {
            envio.setEstado(nuevoEstado);
            if (nuevoEstado == EstadoEnvio.ENTREGADO) {
                envio.setFechaEntregaReal(LocalDateTime.now());
            }
            return envioRepository.save(envio);
        });
    }

    private String generarNumeroSeguimiento() {
        return "EC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}