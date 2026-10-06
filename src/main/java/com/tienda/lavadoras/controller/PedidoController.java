package com.tienda.lavadoras.controller;

import com.tienda.lavadoras.model.Pedido;
import com.tienda.lavadoras.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<Pedido>> obtenerMisPedidos(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String cliente) {
        String usuarioBuscado = (username != null) ? username : cliente;
        return ResponseEntity.ok(pedidoService.obtenerPorCliente(usuarioBuscado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = {"", "/checkout"})
    public ResponseEntity<?> crearPedido(@RequestBody Object body) {
        try {
            System.out.println(">>> CHECKOUT PAYLOAD RECIBIDO: " + body);

            Map<String, Object> payload = new HashMap<>();
            if (body instanceof Map) {
                payload = (Map<String, Object>) body;
            } else if (body instanceof List) {
                payload.put("items", body);
            }

            Pedido nuevoPedido = pedidoService.crearPedidoDesdeMap(payload);
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println(">>> ERROR DE NEGOCIO (400): " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}