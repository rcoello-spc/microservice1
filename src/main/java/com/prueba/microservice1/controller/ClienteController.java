package com.prueba.microservice1.controller;

import com.prueba.microservice1.dto.ClienteDTO;
import com.prueba.microservice1.dto.ClienteResponseDTO;
import com.prueba.microservice1.entity.Cliente;
import com.prueba.microservice1.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gestión de Clientes.
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Obtiene todos los clientes.
     * GET /clientes
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        log.info("GET /clientes - Obteniendo todos los clientes");
        List<ClienteResponseDTO> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Obtiene solo clientes activos.
     * GET /clientes/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ClienteResponseDTO>> getClientesActivos() {
        log.info("GET /clientes/activos - Obteniendo clientes activos");
        List<ClienteResponseDTO> clientes = clienteService.getClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Obtiene cliente por ID.
     * GET /clientes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        log.info("GET /clientes/{} - Obteniendo cliente por ID", id);
        ClienteResponseDTO cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Endpoint interno para obtener entidad completa (usado por microservice2).
     * GET /clientes/{id}/entity
     */
    @GetMapping("/{id}/entity")
    public ResponseEntity<Cliente> getClienteEntityById(@PathVariable Long id) {
        log.info("GET /clientes/{}/entity - Obteniendo entidad cliente por ID", id);
        Cliente cliente = clienteService.getClienteEntityById(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Crea un nuevo cliente.
     * POST /clientes
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        log.info("POST /clientes - Creando nuevo cliente: {}", clienteDTO.nombre());
        ClienteResponseDTO created = clienteService.createCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualiza un cliente existente.
     * PUT /clientes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO clienteDTO) {
        log.info("PUT /clientes/{} - Actualizando cliente", id);
        ClienteResponseDTO updated = clienteService.updateCliente(id, clienteDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Elimina un cliente.
     * DELETE /clientes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.info("DELETE /clientes/{} - Eliminando cliente", id);
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cuenta clientes activos.
     * GET /clientes/count/activos
     */
    @GetMapping("/count/activos")
    public ResponseEntity<Long> countClientesActivos() {
        log.info("GET /clientes/count/activos - Contando clientes activos");
        long count = clienteService.countClientesActivos();
        return ResponseEntity.ok(count);
    }
}
