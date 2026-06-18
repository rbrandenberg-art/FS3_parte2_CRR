package com.SmartLogix.Envio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SmartLogix.Envio.model.Envio;
import com.SmartLogix.Envio.model.EstadoEnvio;
import com.SmartLogix.Envio.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnvioService envioService;

    /**
     * PRUEBA DE CONTROLADOR: POST /api/envios retorna 201 (Created).
     */
    @Test
    public void crearEnvio_SolicitudValida_Retorna201() throws Exception {
        // 1. ARRANGE
        Envio envioEntrada = new Envio();
        envioEntrada.setPedidoId(100L);
        envioEntrada.setDireccionDestino("Avenida Principal 123");
        envioEntrada.setCiudad("Santiago");
        envioEntrada.setPais("Chile");

        Envio envioSalida = new Envio();
        envioSalida.setId(1L);
        envioSalida.setPedidoId(100L);
        envioSalida.setDireccionDestino("Avenida Principal 123");
        envioSalida.setCiudad("Santiago");
        envioSalida.setPais("Chile");
        // Ajusta el nombre del Enum según lo tengas en tu código (ej. PREPARANDO)
        envioSalida.setEstado(EstadoEnvio.valueOf("PREPARANDO")); 

        String tipoDespacho = "Estandar";

        when(envioService.crear(any(Envio.class), eq(tipoDespacho))).thenReturn(envioSalida);

        // 2. ACT & 3. ASSERT
        // Ajusta la ruta "/api/envios" si tu controlador usa otra distinta
        mockMvc.perform(post("/api/envios")
                .param("tipo", tipoDespacho)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioEntrada)))
                
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("PREPARANDO"));
    }

    /**
     * PRUEBA DE CONTROLADOR: PATCH /api/envios/{id}/estado retorna 200 (OK).
     */
    @Test
    public void actualizarEstado_AEntregado_Retorna200() throws Exception {
        // 1. ARRANGE
        Long envioId = 1L;
        // Ajusta el nombre del estado según tu Enum
        EstadoEnvio nuevoEstado = EstadoEnvio.valueOf("ENTREGADO"); 

        Envio envioActualizado = new Envio();
        envioActualizado.setId(envioId);
        envioActualizado.setEstado(nuevoEstado);

        when(envioService.actualizarEstado(eq(envioId), eq(nuevoEstado))).thenReturn(Optional.of(envioActualizado));

        // 2. ACT & 3. ASSERT
        mockMvc.perform(patch("/api/envios/{id}/estado", envioId)
                .param("estado", "ENTREGADO")) // Pasamos el estado como parámetro
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }
}