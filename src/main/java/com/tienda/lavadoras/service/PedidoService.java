package com.tienda.lavadoras.service;

import com.tienda.lavadoras.model.DetallePedido;
import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.model.Pedido;
import com.tienda.lavadoras.repository.LavadoraRepository;
import com.tienda.lavadoras.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private LavadoraRepository lavadoraRepository;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> obtenerPorCliente(String username) {
        if (username == null || username.trim().isEmpty()) {
            return pedidoRepository.findAll();
        }
        return pedidoRepository.findByClienteUsernameOrderByIdDesc(username);
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido crearPedidoDesdeMap(Map<String, Object> payload) {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");

        // 1. Extraer o generar Tiquete / Factura
        String tiquete = null;
        if (payload.containsKey("tiquete")) tiquete = String.valueOf(payload.get("tiquete"));
        else if (payload.containsKey("numFactura")) tiquete = String.valueOf(payload.get("numFactura"));
        else if (payload.containsKey("numeroTiquete")) tiquete = String.valueOf(payload.get("numeroTiquete"));

        if (tiquete == null || tiquete.trim().isEmpty() || "null".equals(tiquete)) {
            tiquete = "FACT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        pedido.setNumeroTiquete(tiquete);

        // 2. Extraer Cliente / Usuario
        String cliente = null;
        if (payload.containsKey("clienteUsername")) cliente = String.valueOf(payload.get("clienteUsername"));
        else if (payload.containsKey("username")) cliente = String.valueOf(payload.get("username"));
        else if (payload.containsKey("usuario")) cliente = String.valueOf(payload.get("usuario"));
        else if (payload.containsKey("cliente")) cliente = String.valueOf(payload.get("cliente"));

        if ((cliente == null || "null".equals(cliente) || cliente.trim().isEmpty()) && payload.get("user") instanceof Map) {
            Map<?, ?> userMap = (Map<?, ?>) payload.get("user");
            if (userMap.containsKey("username")) cliente = String.valueOf(userMap.get("username"));
            else if (userMap.containsKey("usuario")) cliente = String.valueOf(userMap.get("usuario"));
        }

        if (cliente == null || cliente.trim().isEmpty() || "null".equals(cliente)) {
            cliente = "usuario1";
        }
        pedido.setClienteUsername(cliente);

        // 3. Procesar items del carrito
        List<?> itemsRaw = null;
        if (payload.get("items") instanceof List) itemsRaw = (List<?>) payload.get("items");
        else if (payload.get("detalles") instanceof List) itemsRaw = (List<?>) payload.get("detalles");
        else if (payload.get("carrito") instanceof List) itemsRaw = (List<?>) payload.get("carrito");
        else if (payload.get("cart") instanceof List) itemsRaw = (List<?>) payload.get("cart");

        if (itemsRaw == null || itemsRaw.isEmpty()) {
            throw new RuntimeException("El carrito recibido está vacío.");
        }

        double totalCalculado = 0.0;
        List<DetallePedido> detallesGuardar = new ArrayList<>();

        for (Object itemObj : itemsRaw) {
            if (!(itemObj instanceof Map)) continue;
            Map<?, ?> itemMap = (Map<?, ?>) itemObj;

            Long idLavadora = null;
            if (itemMap.containsKey("idLavadora")) idLavadora = parseLong(itemMap.get("idLavadora"));
            else if (itemMap.containsKey("lavadoraId")) idLavadora = parseLong(itemMap.get("lavadoraId"));
            else if (itemMap.containsKey("id")) idLavadora = parseLong(itemMap.get("id"));
            else if (itemMap.get("lavadora") instanceof Map) {
                Map<?, ?> lavMap = (Map<?, ?>) itemMap.get("lavadora");
                if (lavMap.containsKey("id")) idLavadora = parseLong(lavMap.get("id"));
            }

            if (idLavadora == null) {
                throw new RuntimeException("No se encontró el ID de la lavadora.");
            }

            final Long finalId = idLavadora;
            Lavadora lavadora = lavadoraRepository.findById(finalId)
                    .orElseThrow(() -> new RuntimeException("La lavadora con ID " + finalId + " no existe."));

            int cantidadComprada = 1;
            if (itemMap.containsKey("cantidad")) cantidadComprada = parseInt(itemMap.get("cantidad"));
            else if (itemMap.containsKey("quantity")) cantidadComprada = parseInt(itemMap.get("quantity"));

            int stockActual = lavadora.getStock();
            if (stockActual < cantidadComprada) {
                throw new RuntimeException("Stock insuficiente para " + lavadora.getMarca() + ". Disponible: " + stockActual);
            }

            // Descontar únicamente el stock
            lavadora.setStock(stockActual - cantidadComprada);
            lavadoraRepository.save(lavadora);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setLavadora(lavadora);
            detalle.setCantidad(cantidadComprada);
            double precio = (lavadora.getPrecio() != null) ? lavadora.getPrecio() : 0.0;
            detalle.setPrecioUnitario(precio);

            double subtotalItem = precio * cantidadComprada;
            detalle.setSubtotal(subtotalItem);

            detallesGuardar.add(detalle);
            totalCalculado += subtotalItem;
        }

        pedido.setDetalles(detallesGuardar);

        if (payload.containsKey("total")) {
            Double totalPayload = parseDouble(payload.get("total"));
            pedido.setTotal((totalPayload != null && totalPayload > 0) ? totalPayload : totalCalculado);
        } else {
            pedido.setTotal(totalCalculado);
        }

        return pedidoRepository.save(pedido);
    }

    private Long parseLong(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).longValue();
        try { return Long.parseLong(obj.toString()); } catch (Exception e) { return null; }
    }

    private Double parseDouble(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).doubleValue();
        try { return Double.parseDouble(obj.toString()); } catch (Exception e) { return null; }
    }

    private int parseInt(Object obj) {
        if (obj == null) return 1;
        if (obj instanceof Number) return ((Number) obj).intValue();
        try { return Integer.parseInt(obj.toString()); } catch (Exception e) { return 1; }
    }
}