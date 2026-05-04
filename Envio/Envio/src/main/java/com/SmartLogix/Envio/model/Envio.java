package com.SmartLogix.Envio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "envios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long pedidoId;

    @Column(nullable = false)
    private String direccionDestino;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    private String codigoPostal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;

    private String numeroSeguimiento;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEstimadaEntrega;

    private LocalDateTime fechaEntregaReal;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PREPARANDO;
        }
    }
}

