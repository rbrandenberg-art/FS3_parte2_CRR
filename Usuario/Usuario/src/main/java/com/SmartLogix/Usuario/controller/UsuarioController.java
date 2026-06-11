package com.SmartLogix.Usuario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

// IMPORTACIONES DE LOS DTOS NUEVOS Y VALIDACIÓN
import com.SmartLogix.Usuario.dto.UsuarioRequestDTO;
import com.SmartLogix.Usuario.dto.UsuarioResponseDTO;
import com.SmartLogix.Usuario.model.Usuario;
import com.SmartLogix.Usuario.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite la comunicación limpia con tu aplicación en React sin problemas de CORS
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * CAMBIO CONTRATO 3: Adaptado a DTO de Entrada y Salida con Aislamiento Seguro
     * La anotación @Valid activa las restricciones @NotBlank y @Email de tu RequestDTO
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        try {
            UsuarioResponseDTO respuesta = usuarioService.registrar(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException e) {
            // Estructura el JSON de respuesta con la clave "mensaje" para que React lo lea directo
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id,
                                              @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        return usuarioService.desactivar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}