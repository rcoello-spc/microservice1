package com.prueba.microservice1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para Cliente.
 */
public record ClienteDTO(
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100)
        String nombre,

        String genero,

        @Min(value = 0, message = "La edad debe ser positiva")
        int edad,

        @NotBlank(message = "La identificación es obligatoria")
        String identificacion,

        String direccion,

        String telefono,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena,

        String estado
) {
    /**
     * Constructor compacto para validaciones adicionales.
     */
    public ClienteDTO {
        nombre = nombre != null ? nombre.strip() : null;
        estado = estado != null ? estado : "True";
    }

    /**
     * Factory method para crear DTO desde valores básicos.
     */
    public static ClienteDTO of(String nombre, String identificacion, String contrasena) {
        return new ClienteDTO(null, nombre, null, 0, identificacion, null, null, contrasena, "True");
    }

    /**
     * Verifica si el cliente está activo.
     */
    public boolean isActivo() {
        return "True".equalsIgnoreCase(estado);
    }
}
