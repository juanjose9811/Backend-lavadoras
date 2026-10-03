package com.tienda.lavadoras.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroTiquete;
    private String clienteUsername;
    private LocalDateTime fecha;
    private Double total;

    // Campo de estado para seguimiento del pedido
    private String estado = "PENDIENTE";

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles = new ArrayList<>();


    public Pedido() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroTiquete() { return numeroTiquete; }
    public void setNumeroTiquete(String numeroTiquete) { this.numeroTiquete = numeroTiquete; }

    public String getClienteUsername() { return clienteUsername; }
    public void setClienteUsername(String clienteUsername) { this.clienteUsername = clienteUsername; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}