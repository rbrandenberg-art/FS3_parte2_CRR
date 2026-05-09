package com.SmartLogix.Envio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.SmartLogix.Envio.core.CalculadorLogistico;
import com.SmartLogix.Envio.model.Envio;
import com.SmartLogix.Envio.model.EstadoEnvio;
import com.SmartLogix.Envio.repository.EnvioRepository;
import com.SmartLogix.Envio.core.CalculadorLogistico; 
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final List<CalculadorLogistico> modalidadesDespacho;

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

    public Envio crear(Envio envio, String tipoLogistica) {
        envio.setNumeroSeguimiento(generarNumeroSeguimiento());

        // 1. Buscamos la estrategia que coincida con el tipo solicitado
        CalculadorLogistico estrategia = modalidadesDespacho.stream()
            .filter(m -> m.obtenerIdentificador().equalsIgnoreCase(tipoLogistica))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("La modalidad de despacho '" + tipoLogistica + "' no existe."));

        // 2. Ejecutamos la lógica de la estrategia (esto reemplaza el plusDays(5))
        estrategia.procesarPlazos(envio);

        // 3. Guardamos
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