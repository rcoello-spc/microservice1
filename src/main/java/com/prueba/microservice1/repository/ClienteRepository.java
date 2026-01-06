package com.prueba.microservice1.repository;

import com.prueba.microservice1.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}