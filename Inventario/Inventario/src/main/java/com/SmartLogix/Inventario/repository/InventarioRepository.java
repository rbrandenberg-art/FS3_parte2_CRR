package com.SmartLogix.Inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SmartLogix.Inventario.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByCategoria(String categoria);

    List<Inventario> findByStockGreaterThan(Integer stock);
}
