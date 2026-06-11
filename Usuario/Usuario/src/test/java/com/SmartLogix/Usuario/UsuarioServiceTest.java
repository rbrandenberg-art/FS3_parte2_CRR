package com.SmartLogix.Usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.SmartLogix.Usuario.dto.UsuarioRequestDTO;
import com.SmartLogix.Usuario.dto.UsuarioResponseDTO;
import com.SmartLogix.Usuario.model.Usuario;
import com.SmartLogix.Usuario.model.RolUsuario;
import com.SmartLogix.Usuario.repository.UsuarioRepository;
import com.SmartLogix.Usuario.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UsuarioService usuarioService;

    /**
     * PRUEBA 4 (Según tu documento): Verificar que el usuario se guarda correctamente 
     * si el correo electrónico es nuevo y no se encuentra en la base de datos.
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
                .password("password123")
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
        assertEquals("CLIENTE", resultado.getRol());

        // Verificamos que se llamaron a los componentes internos obligatorios
        verify(usuarioRepository, times(1)).existsByEmail("rachell@smartlogix.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(eventPublisher, times(1)).publishEvent(any()); // Verifica que se dispare el evento asíncrono
    }

    /**
     * PRUEBA 5 (Según tu documento): Verificar que si un correo electrónico ya está
     * registrado, el método corta el flujo arrojando un IllegalArgumentException.
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
        verify(usuarioRepository, never()).save(any(Usuario.class)); // El método SAVE jamás debe llamarse si el email se repite
        verify(eventPublisher, never()).publishEvent(any()); // El evento tampoco debe lanzarse
    }
}