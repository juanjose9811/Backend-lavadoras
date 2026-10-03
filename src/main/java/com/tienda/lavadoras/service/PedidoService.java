package com.tienda.lavadoras.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.lavadoras.dto.ItemCarritoDTO;
import com.tienda.lavadoras.dto.PedidoRequestDTO;
import com.tienda.lavadoras.model.DetallePedido;
import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.model.Pedido;
import com.tienda.lavadoras.repository.LavadoraRepository;
import com.tienda.lavadoras.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LavadoraRepository lavadoraRepository;
    private final ObjectMapper objectMapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         LavadoraRepository lavadoraRepository,
                         ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.lavadoraRepository = lavadoraRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Pedido crearPedidoDesdePayload(Map<String, Object> payload, String username) {
        PedidoRequestDTO request = objectMapper.convertValue(payload, PedidoRequestDTO.class);
        return procesarCompra(request, username);
    }

    @Transactional
    public Pedido procesarCompra(PedidoRequestDTO request, String username) {
        Pedido pedido = new Pedido();
        pedido.setClienteUsername(username);

        String tiquete = "TICK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        pedido.setNumeroTiquete(tiquete);

        double totalAcumulado = 0.0;

        for (ItemCarritoDTO item : request.getItems()) {
            Lavadora lavadora = lavadoraRepository.findById(item.getLavadoraId())
                    .orElseThrow(() -> new RuntimeException("Lavadora no encontrada ID: " + item.getLavadoraId()));

            if (lavadora.getCantidad() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + lavadora.getMarca() + " " + lavadora.getModelo());
            }

            lavadora.setCantidad(lavadora.getCantidad() - item.getCantidad());
            lavadoraRepository.save(lavadora);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setLavadora(lavadora);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(lavadora.getPrecio());

            double subtotal = lavadora.getPrecio() * item.getCantidad();
            detalle.setSubtotal(subtotal);

            totalAcumulado += subtotal;
            pedido.getDetalles().add(detalle);
        }

        pedido.setTotal(totalAcumulado);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerPedidosPorUsuario(String username) {
        return pedidoRepository.findByClienteUsernameOrderByIdDesc(username);
    }
}