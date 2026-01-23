package com.prueba.microservice1.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prueba.microservice1.entity.Cliente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Producer de eventos de Cliente para Kafka.
 */
@Component
@Slf4j
public class ClienteEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.cliente-events:cliente-events}")
    private String topicName;

    public ClienteEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Callback para resultado de envío.
     */
    private final BiConsumer<SendResult<String, String>, Throwable> sendCallback = (result, ex) -> {
        if (ex != null) {
            log.error("Error enviando evento a Kafka: {}", ex.getMessage());
        } else {
            log.info("Evento enviado exitosamente a topic: {}, partition: {}, offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        }
    };

    /**
     * Envía evento de cliente creado.
     */
    public void sendClienteCreatedEvent(Cliente cliente) {
        ClienteEvent event = ClienteEvent.created(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );
        sendEvent(event);
    }

    /**
     * Envía evento de cliente actualizado.
     */
    public void sendClienteUpdatedEvent(Cliente cliente) {
        ClienteEvent event = ClienteEvent.updated(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );
        sendEvent(event);
    }

    /**
     * Envía evento de cliente eliminado.
     */
    public void sendClienteDeletedEvent(Long clienteId, String nombreCliente) {
        ClienteEvent event = ClienteEvent.deleted(clienteId, nombreCliente);
        sendEvent(event);
    }

    /**
     * Método genérico para enviar eventos.
     * Ejecuta en hilo separado para no bloquear la operación principal.
     * Fire-and-forget: si Kafka no está disponible, solo registra un warning.
     */
    private void sendEvent(ClienteEvent event) {
        CompletableFuture.runAsync(() -> {
            try {
                String eventJson = objectMapper.writeValueAsString(event);
                String key = String.valueOf(event.clienteId());

                kafkaTemplate.send(topicName, key, eventJson)
                        .whenComplete(sendCallback);

                log.info("Enviando evento {} para cliente ID: {}", event.eventType(), event.clienteId());
            } catch (JsonProcessingException e) {
                log.error("Error serializando evento: {}", e.getMessage());
            } catch (Exception e) {
                log.warn("Kafka no disponible. Evento {} para cliente ID: {} no enviado: {}",
                        event.eventType(), event.clienteId(), e.getMessage());
            }
        });
    }
}
