package com.tienda.lavadoras.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "lavadoras")
public class Lavadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private Double precio;
    private Integer capacidad;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "imagen_url")
    private String imagenUrl;

    public Lavadora() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public Integer getStock() { return stock != null ? stock : 0; }
    public void setStock(Integer stock) { this.stock = stock; }

    // Alias JSON para garantizar compatibilidad si React busca "cantidad" en las tarjetas
    @JsonProperty("cantidad")
    public Integer getCantidadAlias() {
        return getStock();
    }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}