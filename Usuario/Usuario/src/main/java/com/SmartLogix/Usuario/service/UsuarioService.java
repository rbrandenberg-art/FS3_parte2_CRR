package com.SmartLogix.Usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.SmartLogix.Usuario.model.Usuario;
import com.SmartLogix.Usuario.model.UsuarioRegistroEvent;
import com.SmartLogix.Usuario.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private org.springframework.context.ApplicationEventPublisher eventPublisher;
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        Usuario guardado = usuarioRepository.save(usuario);

        
        // Se publica el evento para que cualquier interesado reaccione
        eventPublisher.publishEvent(new UsuarioRegistroEvent(this, guardado.getEmail()));

        return guardado;
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

