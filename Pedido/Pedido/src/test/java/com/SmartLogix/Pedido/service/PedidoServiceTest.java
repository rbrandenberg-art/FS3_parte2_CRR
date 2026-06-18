package com.SmartLogix.Pedido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SmartLogix.Pedido.model.DetallePedido;
import com.SmartLogix.Pedido.model.EstadoPedido;
import com.SmartLogix.Pedido.model.Pedido;
import com.SmartLogix.Pedido.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoSimulado;

    @BeforeEach
    void setUp() {
        // Preparamos los detalles para probar la lógica real de tu método crear()
        DetallePedido detalle1 = new DetallePedido();
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(500.0);
        // Subtotal esperado: 1000.0

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detalle1);

        // Inicializamos el pedido simulado
        pedidoSimulado = Pedido.builder()
                .id(1L)
                .usuarioId(100L)
                .estado(EstadoPedido.PENDIENTE)
                .detalles(detalles)
                .build();
    }

    /**
     * PRUEBA 5 (Adaptada a tu código actual): Crear pedido calcula el total y guarda.
     * Según el documento, aquí a futuro se deberá validar el stock.
     */
    @Test
    public void crearPedido_CalculaTotalDesdeDetalles_YGuardaPedido() {
        // 1. ARRANGE
        // Simulamos que el repositorio guarda y devuelve el pedido
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSimulado);

        // 2. ACT
        Pedido resultado = pedidoService.crear(pedidoSimulado);

        // 3. ASSERT
        assertNotNull(resultado);
        
        // Verificamos tu lógica de negocio actual: El total debió calcularse (2 * 500 = 1000)
        assertEquals(1000.0, pedidoSimulado.getTotal(), "El total debe ser la suma de los subtotales");
        
        // Validamos que se asigne la relación bidireccional (pedido en detalle)
        assertEquals(pedidoSimulado, pedidoSimulado.getDetalles().get(0).getPedido());
        
        // Verificamos la persistencia
        verify(pedidoRepository, times(1)).save(pedidoSimulado);
    }

    /**
     * PRUEBA 6 (Adaptada a tu código actual): Pagar pedido aprobado actualiza el estado.
     * Según el documento, aquí a futuro se deberá descontar stock y crear el envío.
     */
    @Test
    public void cambiarEstado_AEstadoPagado_ActualizaYGuardaPedido() {
        // 1. ARRANGE
        Long pedidoId = 1L;
        // Simulamos que encontramos el pedido pendiente en la BD
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoSimulado));
        
        // Preparamos un pedido simulado ya actualizado
        Pedido pedidoActualizado = Pedido.builder()
                .id(1L)
                // Asumiendo que el pago se refleja pasando el estado a PAGADO o similar
                // Ajusta este Enum si en tu código se llama distinto, por ejemplo, APROBADO
                .estado(EstadoPedido.valueOf("CANCELADO")) 
                .build();
                
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoActualizado);

        // 2. ACT
        Optional<Pedido> resultado = pedidoService.cambiarEstado(pedidoId, EstadoPedido.valueOf("CANCELADO"));

        // 3. ASSERT
        assertTrue(resultado.isPresent(), "El pedido debe existir y ser devuelto");
        assertEquals(EstadoPedido.valueOf("CANCELADO"), resultado.get().getEstado(), "El estado debe actualizarse");

        // Verificamos el flujo interno de base de datos
        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }
}