package com.prueba.microservice1.service;

import com.prueba.microservice1.dto.ClienteDTO;
import com.prueba.microservice1.dto.ClienteResponseDTO;
import com.prueba.microservice1.entity.Cliente;
import com.prueba.microservice1.exception.ClienteNotFoundException;
import com.prueba.microservice1.kafka.ClienteEventProducer;
import com.prueba.microservice1.mapper.ClienteMapper;
import com.prueba.microservice1.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Servicio para gestión de Clientes.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteEventProducer eventProducer;

    private static final Predicate<Cliente> IS_ACTIVE = cliente ->
            cliente.getEstado() != null && cliente.getEstado().equalsIgnoreCase("True");

    private final Consumer<Cliente> logClienteOperation = cliente ->
            log.info("Operación realizada en cliente: {} (ID: {})", cliente.getNombre(), cliente.getId());

    /**
     * Obtiene todos los clientes.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getAllClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    /**
     * Obtiene todos los clientes activos.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getClientesActivos() {
        return clienteRepository.findAll()
                .stream()
                .filter(IS_ACTIVE)
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    /**
     * Obtiene cliente por ID.
     */
    @Transactional(readOnly = true)
    public ClienteResponseDTO getClienteById(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toResponseDTO)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con id: " + id));
    }

    /**
     * Obtiene cliente entity por ID (uso interno).
     */
    @Transactional(readOnly = true)
    public Cliente getClienteEntityById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con id: " + id));
    }

    /**
     * Busca cliente por identificación.
     */
    @Transactional(readOnly = true)
    public Optional<ClienteResponseDTO> findByIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion)
                .map(clienteMapper::toResponseDTO);
    }

    /**
     * Crea un nuevo cliente.
     * Publica evento a Kafka después de crear.
     */
    @Transactional
    public ClienteResponseDTO createCliente(ClienteDTO clienteDTO) {
        log.info("Creando nuevo cliente: {}", clienteDTO.nombre());

        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);

        // Publicar evento a Kafka
        eventProducer.sendClienteCreatedEvent(savedCliente);

        logClienteOperation.accept(savedCliente);
        return clienteMapper.toResponseDTO(savedCliente);
    }

    /**
     * Actualiza un cliente existente.
     * Publica evento a Kafka después de actualizar.
     */
    @Transactional
    public ClienteResponseDTO updateCliente(Long id, ClienteDTO clienteDTO) {
        log.info("Actualizando cliente con id: {}", id);

        return clienteRepository.findById(id)
                .map(cliente -> clienteMapper.updateEntityFromDTO(cliente, clienteDTO))
                .map(clienteRepository::save)
                .map(cliente -> {
                    eventProducer.sendClienteUpdatedEvent(cliente);
                    logClienteOperation.accept(cliente);
                    return clienteMapper.toResponseDTO(cliente);
                })
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con id: " + id));
    }

    /**
     * Elimina un cliente.
     * Publica evento a Kafka después de eliminar.
     */
    @Transactional
    public void deleteCliente(Long id) {
        log.info("Eliminando cliente con id: {}", id);

        clienteRepository.findById(id)
                .ifPresentOrElse(
                        cliente -> {
                            clienteRepository.deleteById(id);
                            eventProducer.sendClienteDeletedEvent(id, cliente.getNombre());
                            log.info("Cliente eliminado: {}", cliente.getNombre());
                        },
                        () -> {
                            throw new ClienteNotFoundException("Cliente no encontrado con id: " + id);
                        }
                );
    }

    /**
     * Verifica si existe un cliente con el ID dado.
     */
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    /**
     * Cuenta el total de clientes activos.
     */
    @Transactional(readOnly = true)
    public long countClientesActivos() {
        return clienteRepository.findAll()
                .stream()
                .filter(IS_ACTIVE)
                .count();
    }
}
