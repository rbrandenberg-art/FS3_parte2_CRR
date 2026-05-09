package com.SmartLogix.Envio.core;

import com.SmartLogix.Envio.model.Envio;

public interface CalculadorLogistico {
    void procesarPlazos(Envio envio);
    String obtenerIdentificador();
}
