package com.tienda.lavadoras.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.lavadoras.dto.ItemCarritoDTO;
import com.tienda.lavadoras.dto.PedidoRequestDTO;
import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.model.Pedido;
import com.tienda.lavadoras.repository.LavadoraRepository;
import com.tienda.lavadoras.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private LavadoraRepository lavadoraRepository;

    // Instancia real sin anotaciones de Mockito (@Mock o @Spy)
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PedidoService pedidoService;

    private Lavadora lavadoraMock;
    private PedidoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Inyección manual directa por constructor
        pedidoService = new PedidoService(pedidoRepository, lavadoraRepository, objectMapper);

        lavadoraMock = new Lavadora();
        lavadoraMock.setId(1L);
        lavadoraMock.setMarca("Whirlpool");
        lavadoraMock.setModelo("WFC80");
        lavadoraMock.setPrecio(1500000.0);
        lavadoraMock.setCantidad(10);
        lavadoraMock.setCapacidad(15.0);

        ItemCarritoDTO itemDTO = new ItemCarritoDTO();
        itemDTO.setLavadoraId(1L);
        itemDTO.setCantidad(2);

        requestDTO = new PedidoRequestDTO();
        requestDTO.setItems(List.of(itemDTO));
    }

    @Test
    @DisplayName("CP01 - Debe procesar la compra correctamente, calcular el total y descontar del inventario")
    void testProcesarCompraExitosa() {
        // Arrange
        when(lavadoraRepository.findById(1L)).thenReturn(Optional.of(lavadoraMock));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Pedido resultado = pedidoService.procesarCompra(requestDTO, "clientePrueba");

        // Assert
        assertNotNull(resultado);
        assertEquals("clientePrueba", resultado.getClienteUsername());
        assertEquals(3000000.0, resultado.getTotal());
        assertEquals(8, lavadoraMock.getCantidad()); // Stock reducido de 10 a 8

        verify(lavadoraRepository, times(1)).save(lavadoraMock);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("CP02 - Debe rechazar la compra si el stock es insuficiente")
    void testProcesarCompraSinStock() {
        // Arrange
        lavadoraMock.setCantidad(1); // Intenta comprar 2 habiendo solo 1
        when(lavadoraRepository.findById(1L)).thenReturn(Optional.of(lavadoraMock));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.procesarCompra(requestDTO, "clientePrueba");
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}