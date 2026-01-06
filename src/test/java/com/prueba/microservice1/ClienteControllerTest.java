package com.prueba.microservice1;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.prueba.microservice1.entity.Cliente;
import com.prueba.microservice1.repository.ClienteRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Test
    public void testCreateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro");
        cliente.setContraseña("1234");
        cliente.setEstado("True");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Pedro\",\"contraseña\":\"1234\",\"estado\":\"True\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Pedro")))
                .andExpect(jsonPath("$.contraseña", is("1234")))
                .andExpect(jsonPath("$.estado", is("True")));
    }
}