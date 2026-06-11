package com.SmartLogix.Inventario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import com.SmartLogix.Inventario.model.Inventario;
import com.SmartLogix.Inventario.repository.InventarioRepository;
import com.SmartLogix.Inventario.service.InventarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository; // Simula la BD

    @InjectMocks
    private InventarioService inventarioService; // Servicio real bajo prueba

    // ==========================================
    // PRUEBA 1: Verificar stock disponible
    // ==========================================
    @Test
    public void hayStock_CuandoExisteCantidadSuficiente_RetornaTrue() {
        // 1. ARRANGE (Patrón AAA - Preparar escenario)
        Long productoId = 1L;
        Integer cantidadSolicitada = 3;
        
        Inventario productoSimulado = new Inventario();
        productoSimulado.setId(productoId);
        productoSimulado.setNombre("Laptop");
        productoSimulado.setStock(10); // Hay 10 en stock

        // Cuando el servicio busque por id, Mockito devolverá el producto simulado
        when(inventarioRepository.findById(productoId)).thenReturn(Optional.of(productoSimulado));

        // 2. ACT (Ejecutar la acción real)
        boolean resultado = inventarioService.hayStock(productoId, cantidadSolicitada);

        // 3. ASSERT (Verificar resultados con JUnit 5)
        assertTrue(resultado, "Debería retornar true porque el stock (10) es mayor a la cantidad (3)");
        verify(inventarioRepository, times(1)).findById(productoId);
    }

    // ==========================================
    // PRUEBA 3 DEL DOCUMENTO: Reducir stock correctamente
    // ==========================================
    @Test
    public void reducirStock_CuandoExisteStockSuficiente_DescuentaYGuardaProducto() {
        // 1. ARRANGE
        Long productoId = 1L;
        Integer cantidadAReducir = 4;

        Inventario productoOriginal = new Inventario();
        productoOriginal.setId(productoId);
        productoOriginal.setNombre("Mouse Ergonómico");
        productoOriginal.setStock(10); // Inicialmente hay 10 unidades

        Inventario productoModificadoSimulado = new Inventario();
        productoModificadoSimulado.setId(productoId);
        productoModificadoSimulado.setNombre("Mouse Ergonómico");
        productoModificadoSimulado.setStock(6); // Resultado tras la resta (10 - 4 = 6)

        // Simulación de los comportamientos de la BD
        when(inventarioRepository.findById(productoId)).thenReturn(Optional.of(productoOriginal));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(productoModificadoSimulado);

        // 2. ACT
        Optional<Inventario> resultadoOpt = inventarioService.reducirStock(productoId, cantidadAReducir);

        // 3. ASSERT
        assertTrue(resultadoOpt.isPresent(), "El resultado debería contener el producto actualizado");
        assertEquals(6, resultadoOpt.get().getStock(), "El stock final debió bajar de 10 a 6");
        
        verify(inventarioRepository, times(1)).findById(productoId);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }
}