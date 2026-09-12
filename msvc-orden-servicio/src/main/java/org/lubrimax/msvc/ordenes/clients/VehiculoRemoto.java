package org.lubrimax.msvc.ordenes.clients;

public class VehiculoRemoto {

    private Long id;
    private Long clienteId;
    private Long kilometrajeActual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(Long kilometrajeActual) {
        this.kilometrajeActual = kilometrajeActual;
    }
}
