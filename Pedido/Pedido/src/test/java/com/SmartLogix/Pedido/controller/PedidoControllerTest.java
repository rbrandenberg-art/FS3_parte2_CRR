package com.SmartLogix.Pedido.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SmartLogix.Pedido.model.DetallePedido;
import com.SmartLogix.Pedido.model.EstadoPedido;
import com.SmartLogix.Pedido.model.Pedido;
import com.SmartLogix.Pedido.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService pedidoService;

    /**
     * PRUEBA DE CONTROLADOR: Crear pedido vía POST retorna 201 (Created).
     */
    @Test
    public void crearPedido_SolicitudValida_Retorna201() throws Exception {
        // 1. ARRANGE
        // Preparamos un detalle de pedido
        DetallePedido detalle = new DetallePedido();
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(500.0);

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detalle);

        // Preparamos el pedido de entrada (simulando el JSON que envía el front)
        Pedido pedidoEntrada = new Pedido();
        pedidoEntrada.setUsuarioId(100L);
        pedidoEntrada.setDetalles(detalles);

        // Preparamos el pedido de salida (lo que devuelve el servicio tras guardar)
        Pedido pedidoSalida = new Pedido();
        pedidoSalida.setId(1L);
        pedidoSalida.setUsuarioId(100L);
        pedidoSalida.setTotal(1000.0);
        pedidoSalida.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoService.crear(any(Pedido.class))).thenReturn(pedidoSalida);

        // 2. ACT & 3. ASSERT
        // IMPORTANTE: Ajusta "/api/pedidos" si la ruta de tu controlador es diferente
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoEntrada)))
                
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.total").value(1000.0))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    /**
     * PRUEBA DE CONTROLADOR: Actualizar estado vía PATCH retorna 200 (OK).
     */
    @Test
    public void cambiarEstado_APagado_Retorna200() throws Exception {
        // 1. ARRANGE
        Long pedidoId = 1L;
        EstadoPedido nuevoEstado = EstadoPedido.valueOf("CANCELADO"); // Ajusta si tu Enum usa otro nombre

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(pedidoId);
        pedidoActualizado.setEstado(nuevoEstado);

        when(pedidoService.cambiarEstado(eq(pedidoId), eq(nuevoEstado))).thenReturn(Optional.of(pedidoActualizado));

        // 2. ACT & 3. ASSERT
        // Simulamos una petición PATCH para cambiar el estado
        mockMvc.perform(patch("/api/pedidos/{id}/estado", pedidoId)
                .param("estado", "CANCELADO")) // Pasamos el parámetro por URL
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"));
    }
}