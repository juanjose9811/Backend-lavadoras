package com.tienda.lavadoras.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "numero_tiquete")
    private String numeroTiquete;

    @Column(name = "cliente_username")
    private String clienteUsername;

    private LocalDateTime fecha;
    private Double total = 0.0;
    private String estado = "PENDIENTE";

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroTiquete() { return numeroTiquete; }
    public void setNumeroTiquete(String numeroTiquete) { this.numeroTiquete = numeroTiquete; }

    // Propiedades JSON mapeadas para que React renderice la factura
    @JsonProperty("tiquete")
    public String getTiquete() { return numeroTiquete; }

    @JsonProperty("numFactura")
    public String getNumFactura() { return numeroTiquete; }

    @JsonProperty("numeroTiquete")
    public String getNumeroTiqueteJson() { return numeroTiquete; }

    public String getClienteUsername() { return clienteUsername; }
    public void setClienteUsername(String clienteUsername) { this.clienteUsername = clienteUsername; }

    @JsonProperty("cliente")
    public String getCliente() { return clienteUsername; }

    @JsonProperty("usuario")
    public String getUsuario() { return clienteUsername; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @JsonProperty("items")
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        if (detalles != null) {
            for (DetallePedido d : detalles) {
                d.setPedido(this);
            }
        }
    }
}