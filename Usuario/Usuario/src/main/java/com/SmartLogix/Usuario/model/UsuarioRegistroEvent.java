package com.SmartLogix.Usuario.model;

public class UsuarioRegistroEvent extends org.springframework.context.ApplicationEvent {
    private String email;

    public UsuarioRegistroEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

    public String getEmail() { return email; }
}