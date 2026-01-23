package com.prueba.microservice1.kafka;

import java.time.LocalDateTime;

/**
 * Evento de Cliente para Kafka.
 */
public record ClienteEvent(
        String eventType,
        Long clienteId,
        String nombreCliente,
        String identificacion,
        String estado,
        LocalDateTime timestamp
) {
    /**
     * Tipos de eventos soportados.
     */
    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    /**
     * Factory method para evento de creación.
     */
    public static ClienteEvent created(Long id, String nombre, String identificacion, String estado) {
        return new ClienteEvent(
                EventType.CREATED.name(),
                id,
                nombre,
                identificacion,
                estado,
                LocalDateTime.now()
        );
    }

    /**
     * Factory method para evento de actualización.
     */
    public static ClienteEvent updated(Long id, String nombre, String identificacion, String estado) {
        return new ClienteEvent(
                EventType.UPDATED.name(),
                id,
                nombre,
                identificacion,
                estado,
                LocalDateTime.now()
        );
    }

    /**
     * Factory method para evento de eliminación.
     */
    public static ClienteEvent deleted(Long id, String nombre) {
        return new ClienteEvent(
                EventType.DELETED.name(),
                id,
                nombre,
                null,
                null,
                LocalDateTime.now()
        );
    }
}
