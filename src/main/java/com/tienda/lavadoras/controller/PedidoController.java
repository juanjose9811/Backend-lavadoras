package com.tienda.lavadoras.controller;

import com.tienda.lavadoras.exception.RecursoNoEncontradoException;
import com.tienda.lavadoras.model.Pedido;
import com.tienda.lavadoras.repository.PedidoRepository;
import com.tienda.lavadoras.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;

    // Inyección de dependencias por constructor
    public PedidoController(PedidoService pedidoService, PedidoRepository pedidoRepository) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    // Procesar checkout
    @PostMapping("/checkout")
    public ResponseEntity<Pedido> realizarCheckout(@RequestBody Map<String, Object> payload, Authentication authentication) {
        String username = authentication.getName();
        Pedido pedido = pedidoService.crearPedidoDesdePayload(payload, username);
        return ResponseEntity.ok(pedido);
    }

    // Historial para el cliente
    @GetMapping("/mis-pedidos")
    public List<Pedido> listarMisPedidos(Authentication authentication) {
        String username = authentication.getName();
        return pedidoRepository.findByClienteUsernameOrderByIdDesc(username);
    }

    // Listar TODOS los pedidos (Para Administrador)
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByIdDesc();
    }

    // Actualizar estado del pedido (Para Administrador)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id));

        if (nuevoEstado != null && !nuevoEstado.trim().isEmpty()) {
            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);
        }
        return ResponseEntity.ok(pedido);
    }
}