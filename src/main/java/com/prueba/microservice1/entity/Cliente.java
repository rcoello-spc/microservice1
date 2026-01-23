package com.prueba.microservice1.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad Cliente que extiende de Persona.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cliente extends Persona {

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    private String estado;

    /**
     * Verifica si el cliente está activo.
     * Null-safe check del estado.
     */
    public boolean isActivo() {
        return estado != null && estado.equalsIgnoreCase("True");
    }
}
