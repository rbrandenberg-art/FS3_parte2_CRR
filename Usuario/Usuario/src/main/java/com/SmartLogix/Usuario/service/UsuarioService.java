package com.SmartLogix.Usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.SmartLogix.Usuario.model.Usuario;
import com.SmartLogix.Usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

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
            throw new IllegalArgumentException("El email ya está registrado: " + usuario.getEmail());
        }
        // En producción: hashear el password antes de guardar
        return usuarioRepository.save(usuario);
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

