package com.SmartLogix.Usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SmartLogix.Usuario.dto.UsuarioRequestDTO;
import com.SmartLogix.Usuario.dto.UsuarioResponseDTO;
import com.SmartLogix.Usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

// @WebMvcTest levanta solo la capa web (Controladores) sin necesidad de base de datos
@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Herramienta principal para simular las peticiones HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para convertir los objetos Java a JSON y viceversa

    @MockBean
    private UsuarioService usuarioService; // Mockeamos el servicio para aislar el controlador

    /**
     * PRUEBA DE CONTROLADOR (Documento): POST /api/usuarios/registrar retorna 201 con AuthResponse.
     * Objetivo: Validar endpoint, JSON de entrada y respuesta de registro.
     */
    @Test
    public void registrarUsuario_SolicitudValida_Retorna201YRespuestaSinPassword() throws Exception {
        // 1. ARRANGE (Preparar el escenario)
        // Simulamos el JSON que enviaría React al formulario de registro
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "Rachell",
                "Chavarria",
                "rachell@smartlogix.com",
                "password123",
                "+56911112222"
        );

        // Preparamos la respuesta esperada usando tu UsuarioResponseDTO real
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(
                1L,
                "Rachell",
                "Chavarria",
                "rachell@smartlogix.com",
                "CLIENTE" // Tu DTO espera un String, así que pasamos el rol directamente
        );

        // Le decimos al mock qué devolver cuando el controlador llame al servicio
        when(usuarioService.registrar(any(UsuarioRequestDTO.class))).thenReturn(responseDTO);

        // 2. ACT & 3. ASSERT (Ejecutar petición HTTP y validar respuesta)
        mockMvc.perform(post("/api/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))) // Convertimos el request a JSON
                
                // Verificamos que el código HTTP sea 201 (Created)
                .andExpect(status().isCreated()) 
                
                // Verificamos que el cuerpo de la respuesta JSON tenga los valores correctos
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Rachell"))
                .andExpect(jsonPath("$.email").value("rachell@smartlogix.com"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"))
                
                // REGLA DE SEGURIDAD CRÍTICA DEL DOCUMENTO:
                // Verificamos explícitamente que la contraseña NO exista en el JSON de respuesta
                .andExpect(jsonPath("$.password").doesNotExist()); 
    }
}