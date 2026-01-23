package com.prueba.microservice1.repository;

import com.prueba.microservice1.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones CRUD de Cliente.
 * Extiende JpaRepository para funcionalidad automática.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca cliente por identificación.
     */
    Optional<Cliente> findByIdentificacion(String identificacion);

    /**
     * Busca clientes por estado.
     */
    List<Cliente> findByEstado(String estado);

    /**
     * Busca clientes activos.
     */
    @Query("SELECT c FROM Cliente c WHERE UPPER(c.estado) = 'TRUE'")
    List<Cliente> findAllActivos();

    /**
     * Busca clientes por nombre (búsqueda parcial, case insensitive).
     */
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Verifica si existe cliente con la identificación dada.
     */
    boolean existsByIdentificacion(String identificacion);
}
