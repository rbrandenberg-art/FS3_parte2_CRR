package com.SmartLogix.Envio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SmartLogix.Envio.model.Envio;
import com.SmartLogix.Envio.model.EstadoEnvio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Optional<Envio> findByPedidoId(Long pedidoId);

    Optional<Envio> findByNumeroSeguimiento(String numeroSeguimiento);

    List<Envio> findByEstado(EstadoEnvio estado);
}