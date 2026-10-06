package com.tienda.lavadoras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "lavadora_id", nullable = false)
    private Lavadora lavadora;

    private Integer cantidad = 1;

    @Column(name = "precio_unitario")
    private Double precioUnitario = 0.0;

    private Double subtotal = 0.0; // <-- Campo obligatorio de la base de datos

    public DetallePedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Lavadora getLavadora() { return lavadora; }
    public void setLavadora(Lavadora lavadora) { this.lavadora = lavadora; }

    public Integer getCantidad() { return cantidad != null ? cantidad : 1; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) { this.cantidad = quantity; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}