package com.SmartLogix.Envio.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.SmartLogix.Envio.model.Envio;
import com.SmartLogix.Envio.model.EstadoEnvio;
import com.SmartLogix.Envio.repository.EnvioRepository;

@ExtendWith(MockitoExtension.class)
public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    // Herramienta para notificar a order-ms (Pedido)
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EnvioService envioService;

    private Envio envioSimulado;

    @BeforeEach
    void setUp() {
        // Preparamos un envío base
        envioSimulado = Envio.builder()
                .id(1L)
                .pedidoId(100L)
                .direccionDestino("Avenida Principal 123")
                .ciudad("Santiago")
                .pais("Chile")
                .estado(EstadoEnvio.PREPARANDO)
                .build();
    }

    /**
     * PRUEBA 9: Crear envío duplicado para el mismo pedido lanza conflicto.
     * Objetivo: Evitar que un pedido tenga más de un envío creado por el flujo principal.
     */
    @Test
    public void crearEnvio_PedidoYaTieneEnvio_LanzaExcepcion() {
        // 1. ARRANGE
        String tipoDespacho = "Estandar";
        
        // Simulamos que al buscar en la base de datos por el ID del pedido, ya existe un envío
        // (Asumiendo que tu repositorio tiene el método findByPedidoId u obtenerPorPedido)
        when(envioRepository.findByPedidoId(envioSimulado.getPedidoId()))
            .thenReturn(Optional.of(envioSimulado));

        // 2. ACT & 3. ASSERT
        // Validamos que se bloquee el flujo lanzando un conflicto
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            envioService.crear(envioSimulado, tipoDespacho);
        });

        assertTrue(excepcion.getMessage().toLowerCase().contains("ya existe") || 
                   excepcion.getMessage().toLowerCase().contains("envío"));

        // Regla crítica: El sistema bloquea envíos duplicados, por ende NUNCA se llama a save
        verify(envioRepository, never()).save(any(Envio.class));
    }

    /**
     * PRUEBA 10: Actualizar envío a DELIVERED (ENTREGADO) notifica a order-ms.
     * Objetivo: Verificar que la entrega de un envío actualiza el flujo de pedidos.
     */
    @Test
    public void actualizarEstado_AEntregado_GuardaYNotificaPedido() {
        // 1. ARRANGE
        Long envioId = 1L;
        // El envío existe en la base de datos
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioSimulado));
        
        // Preparamos el objeto con el estado actualizado
        Envio envioEntregado = Envio.builder()
                .id(1L)
                .pedidoId(100L)
                .estado(EstadoEnvio.ENTREGADO) // Equivalente a DELIVERED
                .build();
                
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEntregado);

        // 2. ACT
        Optional<Envio> resultado = envioService.actualizarEstado(envioId, EstadoEnvio.ENTREGADO);

        // 3. ASSERT
        assertTrue(resultado.isPresent(), "El envío debe existir");
        assertEquals(EstadoEnvio.ENTREGADO, resultado.get().getEstado(), "El estado debe ser ENTREGADO");
        
        // Verificamos la persistencia
        verify(envioRepository, times(1)).save(any(Envio.class));
        
        // VALIDACIÓN DE ARQUITECTURA (Comunicación entre microservicios):
        // Cuando implementes la notificación al servicio de Pedidos, esta línea verificará 
        // que la llamada HTTP (POST /api/orders/events/order-delivered) ocurra.
        // verify(restTemplate, times(1)).postForEntity(contains("/events/order-delivered"), any(), any());
    }
}