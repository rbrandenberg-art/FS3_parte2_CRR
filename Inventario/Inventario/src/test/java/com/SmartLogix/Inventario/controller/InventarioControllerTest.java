package com.SmartLogix.Inventario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SmartLogix.Inventario.model.Inventario;
import com.SmartLogix.Inventario.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(InventarioController.class)
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventarioService inventarioService;

    /**
     * PRUEBA DE CONTROLADOR: POST /api/inventario/inventario retorna producto creado.
     */
    @Test
    public void crearProducto_SolicitudValida_Retorna201() throws Exception {
        // 1. ARRANGE
        Inventario productoEntrada = new Inventario();
        productoEntrada.setNombre("Monitor Dell");
        productoEntrada.setDescripcion("Monitor 4K");
        productoEntrada.setPrecio(450.0);
        productoEntrada.setStock(10);
        productoEntrada.setCategoria("Monitores");

        Inventario productoSalida = new Inventario();
        productoSalida.setId(1L);
        productoSalida.setNombre("Monitor Dell");
        productoSalida.setDescripcion("Monitor 4K");
        productoSalida.setPrecio(450.0);
        productoSalida.setStock(10);
        productoSalida.setCategoria("Monitores");

        when(inventarioService.guardar(any(Inventario.class))).thenReturn(productoSalida);

        // 2. ACT & 3. ASSERT
        mockMvc.perform(post("/api/inventario/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoEntrada)))
                
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Monitor Dell"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    /**
     * PRUEBA DE CONTROLADOR: GET .../{id}/stock retorna disponibilidad.
     */
    @Test
    public void verificarStock_ConCantidadValida_RetornaDisponibilidad() throws Exception {
        // 1. ARRANGE
        Long productoId = 1L;
        Integer cantidadSolicitada = 2;
        
        when(inventarioService.hayStock(eq(productoId), eq(cantidadSolicitada))).thenReturn(true);

        // 2. ACT & 3. ASSERT
        mockMvc.perform(get("/api/inventario/inventario/{id}/stock", productoId)
                .param("cantidad", String.valueOf(cantidadSolicitada)))
                
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}