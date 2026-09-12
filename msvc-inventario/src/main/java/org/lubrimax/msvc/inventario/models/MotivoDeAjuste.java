package org.lubrimax.msvc.inventario.models;

public enum MotivoDeAjuste {
    ROTURA,
    MERMA,
    ERROR_DIGITACION,
    DIFERENCIA_INVENTARIO,
    NINGUNO // Para cuando el movimiento no es un ajuste (ej. Ingreso por compra o Consumo)
}
