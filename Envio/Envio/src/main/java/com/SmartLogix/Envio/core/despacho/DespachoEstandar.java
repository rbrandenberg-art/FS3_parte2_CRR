package com.SmartLogix.Envio.core.despacho;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.SmartLogix.Envio.core.CalculadorLogistico;
import com.SmartLogix.Envio.model.Envio;

@Component
public class DespachoEstandar implements CalculadorLogistico {
    @Override
    public void procesarPlazos(Envio envio) {
        envio.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(5));
    }

    @Override
    public String obtenerIdentificador() {
        return "ESTANDAR";
    }
}