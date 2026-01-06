package com.prueba.microservice1.entity;

import jakarta.persistence.*;

@Entity
public class Cliente extends Persona {

    private String contraseña;
    private String estado;

    // Getters
    public String getContraseña() {
        return contraseña;
    }

    public String getEstado() {
        return estado;
    }

    // Setters
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}