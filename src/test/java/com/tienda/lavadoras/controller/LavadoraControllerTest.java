package com.tienda.lavadoras.controller;

import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.repository.LavadoraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LavadoraControllerTest {

    @Mock
    private LavadoraRepository repository;

    @InjectMocks
    private LavadoraController controller;

    private Lavadora lavadoraPrueba;

    @BeforeEach
    void setUp() {
        lavadoraPrueba = new Lavadora();
        lavadoraPrueba.setId(1L);
        lavadoraPrueba.setMarca("LG");
        lavadoraPrueba.setModelo("TurboWash 2026");
        lavadoraPrueba.setPrecio(2500000.0);
        lavadoraPrueba.setCantidad(5);
        lavadoraPrueba.setCapacidad(18.0);
    }

    @Test
    @DisplayName("Debe listar todas las lavadoras exitosamente")
    void testListarLavadoras() {
        when(repository.findAll()).thenReturn(Arrays.asList(lavadoraPrueba));

        List<Lavadora> resultado = controller.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("LG", resultado.get(0).getMarca());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe guardar una lavadora exitosamente")
    void testGuardarLavadora() {
        when(repository.save(any(Lavadora.class))).thenReturn(lavadoraPrueba);

        Lavadora resultado = controller.guardar(lavadoraPrueba);

        assertNotNull(resultado);
        assertEquals("LG", resultado.getMarca());
        assertEquals("TurboWash 2026", resultado.getModelo());
        verify(repository, times(1)).save(lavadoraPrueba);
    }
}