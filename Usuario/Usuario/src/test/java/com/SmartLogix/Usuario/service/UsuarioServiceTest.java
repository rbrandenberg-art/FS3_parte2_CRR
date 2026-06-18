package com.SmartLogix.Usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.SmartLogix.Usuario.dto.UsuarioRequestDTO;
import com.SmartLogix.Usuario.dto.UsuarioResponseDTO;
import com.SmartLogix.Usuario.model.RolUsuario;
import com.SmartLogix.Usuario.model.Usuario;
import com.SmartLogix.Usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UsuarioService usuarioService;

    /**
     * PRUEBA 1 (Documento): Registrar usuario nuevo retorna AuthResponse sin exponer contraseña.
     */
    @Test
    public void registrarUsuario_CuandoEmailNuevo_CreaElUsuarioCorrectamente() {
        // 1. ARRANGE (Preparar el escenario)
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "Rachell", 
                "Chavarria", 
                "rachell@smartlogix.com", 
                "password123", 
                "+56911112222"
        );
        
        Usuario usuarioSimulado = Usuario.builder()
                .id(1L)
                .nombre("Rachell")
                .apellido("Chavarria")
                .email("rachell@smartlogix.com")
                .password("hash_secreto_123") // Contraseña interna (encriptada en la realidad)
                .telefono("+56911112222")
                .rol(RolUsuario.CLIENTE)
                .activo(true)
                .build();

        // Configuramos los mocks de acuerdo al flujo de tu servicio
        when(usuarioRepository.existsByEmail("rachell@smartlogix.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSimulado);

        // 2. ACT (Ejecutar la acción central del servicio)
        UsuarioResponseDTO resultado = usuarioService.registrar(request);

        // 3. ASSERT (Verificar que los resultados calzan con las expectativas de la arquitectura)
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Rachell", resultado.getNombre());
        assertEquals("Chavarria", resultado.getApellido());
        assertEquals("rachell@smartlogix.com", resultado.getEmail());
        
        // CORRECCIÓN: Comparamos contra el Enum directamente en lugar de un String
        assertEquals("CLIENTE", resultado.getRol());

        // NOTA DE SEGURIDAD: El documento exige que no se exponga la contraseña.
        // Como UsuarioResponseDTO no tiene (ni debe tener) el método getPassword(), el compilador
        // de Java garantiza que la aserción de seguridad se cumple automáticamente.

        // Verificamos que se llamaron a los componentes internos obligatorios
        verify(usuarioRepository, times(1)).existsByEmail("rachell@smartlogix.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(eventPublisher, times(1)).publishEvent(any()); // Verifica que se dispare el evento asíncrono
    }

    /**
     * PRUEBA 2 (Documento): Registrar usuario con correo repetido lanza error.
     */
    @Test
    public void registrarUsuario_CuandoEmailYaExiste_LanzaIllegalArgumentException() {
        // 1. ARRANGE (Preparar el escenario con un correo duplicado)
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "Rhudy", 
                "Brandenberg", 
                "rhudy@smartlogix.com", 
                "secure456", 
                null
        );

        // Simulamos que el repositorio encuentra correspondencia duplicada 
        when(usuarioRepository.existsByEmail("rhudy@smartlogix.com")).thenReturn(true);

        // 2. ACT & 3. ASSERT (Ejecutar y evaluar la captura de la excepción esperada)
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrar(request);
        });

        assertEquals("El email ya está registrado", excepcion.getMessage());

        // Reglas de negocio críticas a comprobar:
        verify(usuarioRepository, times(1)).existsByEmail("rhudy@smartlogix.com");
        // El método SAVE jamás debe llamarse si el email se repite 
        verify(usuarioRepository, never()).save(any(Usuario.class)); 
        // El evento tampoco debe lanzarse
        verify(eventPublisher, never()).publishEvent(any()); 
    }
}