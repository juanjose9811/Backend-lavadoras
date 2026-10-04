package com.tienda.lavadoras.service;

import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.repository.LavadoraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LavadoraServiceTest {

    @Mock
    private LavadoraRepository lavadoraRepository;

    @InjectMocks
    private LavadoraService lavadoraService;

    private Lavadora lavadora;

    @BeforeEach
    void setUp() {
        lavadora = new Lavadora();
        lavadora.setId(1L);
        lavadora.setMarca("Samsung");
        lavadora.setModelo("WW90T");
        lavadora.setPrecio(2500000.0);
        lavadora.setCantidad(10);
        lavadora.setCapacidad(9.0);
    }

    @Test
    void testObtenerTodasLasLavadoras() {
        when(lavadoraRepository.findAll()).thenReturn(Arrays.asList(lavadora));

        List<Lavadora> resultado = lavadoraService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Samsung", resultado.get(0).getMarca());
        verify(lavadoraRepository, times(1)).findAll();
    }

    @Test
    void testGuardarLavadoraExitosamente() {
        when(lavadoraRepository.save(any(Lavadora.class))).thenReturn(lavadora);

        Lavadora guardada = lavadoraService.guardar(lavadora);

        assertNotNull(guardada);
        assertEquals("WW90T", guardada.getModelo());
        verify(lavadoraRepository, times(1)).save(lavadora);
    }

    @Test
    void testBuscarPorIdExistente() {
        when(lavadoraRepository.findById(1L)).thenReturn(Optional.of(lavadora));

        Optional<Lavadora> resultado = lavadoraService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(2500000.0, resultado.get().getPrecio());
    }
}