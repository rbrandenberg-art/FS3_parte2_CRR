package com.SmartLogix.Inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.SmartLogix.Inventario.model.Inventario;
import com.SmartLogix.Inventario.repository.InventarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> obtenerPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Optional<Inventario> actualizar(Long id, Inventario inventarioActualizado) {
        return inventarioRepository.findById(id).map(inventario -> {
            inventario.setNombre(inventarioActualizado.getNombre());
            inventario.setDescripcion(inventarioActualizado.getDescripcion());
            inventario.setPrecio(inventarioActualizado.getPrecio());
            inventario.setStock(inventarioActualizado.getStock());
            inventario.setCategoria(inventarioActualizado.getCategoria());
            return inventarioRepository.save(inventario);
        });
    }

    public boolean eliminar(Long id) {
        if (inventarioRepository.existsById(id)) {
            inventarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean hayStock(Long id, Integer cantidad) {
        return inventarioRepository.findById(id)
                .map(p -> p.getStock() >= cantidad)
                .orElse(false);
    }

    public Optional<Inventario> reducirStock(Long id, Integer cantidad) {
        return inventarioRepository.findById(id).map(inventario -> {
            inventario.setStock(inventario.getStock() - cantidad);
            return inventarioRepository.save(inventario);
        });
    }
}

