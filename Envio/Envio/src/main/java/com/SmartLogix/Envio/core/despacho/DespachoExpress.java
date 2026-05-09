package com.SmartLogix.Envio.core.despacho;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.SmartLogix.Envio.core.CalculadorLogistico;
import com.SmartLogix.Envio.model.Envio;

@Component
public class DespachoExpress implements CalculadorLogistico {
    @Override
    public void procesarPlazos(Envio envio) {
        envio.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(1));
    }

    @Override
    public String obtenerIdentificador() {
        return "EXPRESS";
    }
}