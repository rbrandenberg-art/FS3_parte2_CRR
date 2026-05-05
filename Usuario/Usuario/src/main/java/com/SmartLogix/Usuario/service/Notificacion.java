package com.SmartLogix.Usuario.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.SmartLogix.Usuario.model.UsuarioRegistroEvent;

@Component
public class Notificacion{

    @EventListener
    public void alRegistrarUsuario(UsuarioRegistroEvent evento) {
        // Esta es la reacción del Observer (ej. enviar a Kafka o Log)
        System.out.println("[OBSERVER] Se detectó registro de: " + evento.getEmail());
        System.out.println("[OBSERVER] Sincronizando datos con el API Gateway...");
    }
}