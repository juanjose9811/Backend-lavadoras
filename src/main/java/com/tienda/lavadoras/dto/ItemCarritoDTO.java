package com.tienda.lavadoras.dto;

public class ItemCarritoDTO {
    private Long lavadoraId;
    private Integer cantidad;

    public Long getLavadoraId() { return lavadoraId; }
    public void setLavadoraId(Long lavadoraId) { this.lavadoraId = lavadoraId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}