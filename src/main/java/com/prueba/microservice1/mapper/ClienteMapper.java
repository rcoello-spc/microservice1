package com.prueba.microservice1.mapper;

import com.prueba.microservice1.dto.ClienteDTO;
import com.prueba.microservice1.dto.ClienteResponseDTO;
import com.prueba.microservice1.entity.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * Mapper para conversiones entre Cliente y DTOs.
 */
@Component
public class ClienteMapper {

    public static final Function<Cliente, ClienteResponseDTO> TO_RESPONSE_DTO = ClienteResponseDTO::fromEntity;

    public static final Function<ClienteDTO, Cliente> TO_ENTITY = dto -> {
        Cliente cliente = new Cliente();
        cliente.setId(dto.id());
        cliente.setNombre(dto.nombre());
        cliente.setGenero(dto.genero());
        cliente.setEdad(dto.edad());
        cliente.setIdentificacion(dto.identificacion());
        cliente.setDireccion(dto.direccion());
        cliente.setTelefono(dto.telefono());
        cliente.setContrasena(dto.contrasena());
        cliente.setEstado(dto.estado());
        return cliente;
    };

    /**
     * Convierte entidad a DTO de respuesta.
     */
    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return TO_RESPONSE_DTO.apply(cliente);
    }

    /**
     * Convierte DTO a entidad.
     */
    public Cliente toEntity(ClienteDTO dto) {
        return TO_ENTITY.apply(dto);
    }

    /**
     * Convierte lista de entidades a lista de DTOs.
     */
    public List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes) {
        return clientes.stream()
                .map(TO_RESPONSE_DTO)
                .toList();
    }

    /**
     * Actualiza una entidad existente con datos del DTO.
     */
    public Cliente updateEntityFromDTO(Cliente cliente, ClienteDTO dto) {
        // Solo actualiza campos no nulos del DTO
        if (dto.nombre() != null) cliente.setNombre(dto.nombre());
        if (dto.genero() != null) cliente.setGenero(dto.genero());
        if (dto.edad() > 0) cliente.setEdad(dto.edad());
        if (dto.direccion() != null) cliente.setDireccion(dto.direccion());
        if (dto.telefono() != null) cliente.setTelefono(dto.telefono());
        if (dto.contrasena() != null) cliente.setContrasena(dto.contrasena());
        if (dto.estado() != null) cliente.setEstado(dto.estado());
        return cliente;
    }
}
