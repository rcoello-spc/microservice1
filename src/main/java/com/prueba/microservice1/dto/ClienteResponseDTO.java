package com.prueba.microservice1.dto;

import com.prueba.microservice1.entity.Cliente;

/**
 * DTO de respuesta para Cliente (sin exponer contraseña).
 */
public record ClienteResponseDTO(
        Long id,
        String nombre,
        String genero,
        int edad,
        String identificacion,
        String direccion,
        String telefono,
        String estado
) {
    /**
     * Convierte entidad a DTO.
     */
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getEstado()
        );
    }
}
