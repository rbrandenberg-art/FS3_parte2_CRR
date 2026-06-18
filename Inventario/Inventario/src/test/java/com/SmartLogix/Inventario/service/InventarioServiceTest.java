package com.SmartLogix.Inventario.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.SmartLogix.Inventario.model.Inventario;
import com.SmartLogix.Inventario.repository.InventarioRepository;
import com.SmartLogix.Inventario.service.InventarioService;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository; // Simula la BD

    @Mock
    private ApplicationEventPublisher eventPublisher; // NECESARIO para la Prueba 3 del documento

    @InjectMocks
    private InventarioService inventarioService; // Servicio real bajo prueba

    // ==========================================
    // PRUEBA 3 DEL DOCUMENTO: Crear producto registra stock inicial y alerta stock bajo
    // ==========================================
    @Test
    public void crearProducto_ConStockBajo_GuardaYPublicaEvento() {
        // 1. ARRANGE
        Inventario productoConStockBajo = new Inventario();
        productoConStockBajo.setStock(3); // Menor a 5 para disparar la alerta

        // IMPORTANTE: Aquí simulamos el repositorio. Fíjate que devolvemos un objeto Inventario, no un String.
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(productoConStockBajo);

        // 2. ACT
        Inventario guardado = inventarioService.guardar(productoConStockBajo);

        // 3. ASSERT
        // Aquí verificamos el repositorio
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        
        // Aquí verificamos que se publicó el evento enviando un texto (String)
        verify(eventPublisher, times(1)).publishEvent(any(String.class));
        
        assertEquals(3, guardado.getStock());
    }

    // ==========================================
    // PRUEBA 4 DEL DOCUMENTO: Descontar stock insuficiente lanza conflicto
    // ==========================================
    @Test
    public void reducirStock_ConStockInsuficiente_LanzaExcepcion() {
        // 1. ARRANGE
        Long productoId = 1L;
        Integer cantidadAReducir = 15; // Pedimos 15

        Inventario productoOriginal = new Inventario();
        productoOriginal.setId(productoId);
        productoOriginal.setNombre("Mouse Ergonómico");
        productoOriginal.setStock(10); // Pero solo hay 10 en stock

        when(inventarioRepository.findById(productoId)).thenReturn(Optional.of(productoOriginal));

        // 2. ACT & 3. ASSERT
        // Validamos que lanzar la acción genera una excepción para evitar stock negativo
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.reducirStock(productoId, cantidadAReducir);
        });

        // Asegurarnos de que el mensaje de error mencione el stock insuficiente
        assertTrue(excepcion.getMessage().toLowerCase().contains("stock"));

        verify(inventarioRepository, times(1)).findById(productoId);
        // REGLA CRÍTICA: Nunca debe llamar a guardar en la BD si el stock es insuficiente
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    // ==========================================
    // PRUEBA EXTRA (La que tú hiciste): Reducir stock cuando SÍ hay suficiente
    // (Es excelente mantenerla porque valida el caso feliz)
    // ==========================================
    @Test
    public void reducirStock_CuandoExisteStockSuficiente_DescuentaYGuardaProducto() {
        // 1. ARRANGE
        Long productoId = 1L;
        Integer cantidadAReducir = 4;

        Inventario productoOriginal = new Inventario();
        productoOriginal.setId(productoId);
        productoOriginal.setNombre("Mouse Ergonómico");
        productoOriginal.setStock(10); 

        Inventario productoModificadoSimulado = new Inventario();
        productoModificadoSimulado.setId(productoId);
        productoModificadoSimulado.setNombre("Mouse Ergonómico");
        productoModificadoSimulado.setStock(6); 

        when(inventarioRepository.findById(productoId)).thenReturn(Optional.of(productoOriginal));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(productoModificadoSimulado);

        // 2. ACT
        Optional<Inventario> resultadoOpt = inventarioService.reducirStock(productoId, cantidadAReducir);

        // 3. ASSERT
        assertTrue(resultadoOpt.isPresent());
        assertEquals(6, resultadoOpt.get().getStock());
        
        verify(inventarioRepository, times(1)).findById(productoId);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }
}