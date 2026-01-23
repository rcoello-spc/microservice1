package com.prueba.microservice1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad base Persona.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 10, message = "El género no puede exceder 10 caracteres")
    private String genero;

    @Min(value = 0, message = "La edad debe ser un número positivo")
    private int edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Column(unique = true)
    private String identificacion;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
}
