package com.prueba.microservice1.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la entidad de dominio Cliente.
 * Valida el comportamiento de la entidad, herencia y sus atributos.
 */
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    @DisplayName("Cliente debe heredar atributos de Persona")
    void clienteDebeHeredarDePersona() {
        // Verificar que Cliente es instancia de Persona (herencia)
        assertTrue(cliente instanceof Persona,
                "Cliente debe heredar de la clase Persona");
    }

    @Test
    @DisplayName("Debe establecer y obtener atributos de Persona correctamente")
    void debeEstablecerAtributosDePersona() {
        // Datos de prueba
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("M");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");

        // Verificaciones
        assertEquals(1L, cliente.getId());
        assertEquals("Jose Lema", cliente.getNombre());
        assertEquals("M", cliente.getGenero());
        assertEquals(30, cliente.getEdad());
        assertEquals("1234567890", cliente.getIdentificacion());
        assertEquals("Otavalo sn y principal", cliente.getDireccion());
        assertEquals("098254785", cliente.getTelefono());
    }

    @Test
    @DisplayName("Debe establecer y obtener atributos propios de Cliente")
    void debeEstablecerAtributosDeCliente() {
        // Datos de prueba
        cliente.setContraseña("1234");
        cliente.setEstado("True");

        // Verificaciones
        assertEquals("1234", cliente.getContraseña());
        assertEquals("True", cliente.getEstado());
    }

    @Test
    @DisplayName("Debe crear cliente completo con todos los atributos")
    void debeCrearClienteCompleto() {
        // Configurar cliente completo (como en los casos de uso)
        cliente.setNombre("Marianela Montalvo");
        cliente.setGenero("F");
        cliente.setEdad(28);
        cliente.setIdentificacion("1234567891");
        cliente.setDireccion("Amazonas y NNUU");
        cliente.setTelefono("097548965");
        cliente.setContraseña("5678");
        cliente.setEstado("True");

        // Verificar que todos los campos estan configurados
        assertNotNull(cliente.getNombre());
        assertNotNull(cliente.getGenero());
        assertNotNull(cliente.getIdentificacion());
        assertNotNull(cliente.getDireccion());
        assertNotNull(cliente.getTelefono());
        assertNotNull(cliente.getContraseña());
        assertNotNull(cliente.getEstado());
    }

    @Test
    @DisplayName("Cliente nuevo debe tener atributos nulos por defecto")
    void clienteNuevoDebeSerNulo() {
        Cliente nuevoCliente = new Cliente();

        assertNull(nuevoCliente.getId());
        assertNull(nuevoCliente.getNombre());
        assertNull(nuevoCliente.getContraseña());
        assertNull(nuevoCliente.getEstado());
    }
}
