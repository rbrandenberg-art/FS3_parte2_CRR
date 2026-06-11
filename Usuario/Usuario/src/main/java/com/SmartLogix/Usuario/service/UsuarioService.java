package com.SmartLogix.Usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// IMPORTACIONES DE LOS DTOS NUEVOS
import com.SmartLogix.Usuario.dto.UsuarioRequestDTO;
import com.SmartLogix.Usuario.dto.UsuarioResponseDTO;
import com.SmartLogix.Usuario.model.Usuario;
import com.SmartLogix.Usuario.model.RolUsuario; // Enum de tu entidad
import com.SmartLogix.Usuario.model.UsuarioRegistroEvent;
import com.SmartLogix.Usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok genera el constructor para la inyección automática
public class UsuarioService {

    // Al quitar @Autowired y dejarlos como "final", Lombok los inyecta de forma segura
    private final UsuarioRepository usuarioRepository;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     		* CAMBIO CONTRATO 3: Registro adaptado a DTO de Entrada y Salida
     		*/
    @Transactional
    public UsuarioResponseDTO registrar(UsuarioRequestDTO requestDTO) {
        // 1. Validar si el email ya existe (Exigencia estricta de la Prueba 5)
        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // 2. Convertir el DTO de entrada en la Entidad usando el Builder de tu clase Usuario
        Usuario usuario = Usuario.builder()
                .nombre(requestDTO.getNombre())
                .apellido(requestDTO.getApellido())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword()) // En producción aquí aplicarías BCrypt
                .telefono(requestDTO.getTelefono())
                .rol(RolUsuario.CLIENTE) // Se asigna el Enum requerido por tu entidad
                .activo(true)
                .build();
        
        // 3. Guardar en la base de datos
        Usuario guardado = usuarioRepository.save(usuario);

        // 4. Publicar el evento original que ya tenías programado (Prueba 4)
        eventPublisher.publishEvent(new UsuarioRegistroEvent(this, guardado.getEmail()));

        // 5. Retornar el DTO de salida libre de password y activo para proteger los datos
        return new UsuarioResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getEmail(),
                guardado.getRol().name() // Convertimos el Enum a String para el Frontend
        );
    }

    public Optional<Usuario> actualizar(Long id, Usuario datosActualizados) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datosActualizados.getNombre());
            usuario.setApellido(datosActualizados.getApellido());
            usuario.setTelefono(datosActualizados.getTelefono());
            return usuarioRepository.save(usuario);
        });
    }

    public boolean desactivar(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            return true;
        }).orElse(false);
    }
}