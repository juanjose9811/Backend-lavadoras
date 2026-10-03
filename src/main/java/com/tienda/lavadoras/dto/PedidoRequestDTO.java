package com.tienda.lavadoras.dto;

import java.util.List;

public class PedidoRequestDTO {
    private List<ItemCarritoDTO> items;

    public List<ItemCarritoDTO> getItems() { return items; }
    public void setItems(List<ItemCarritoDTO> items) { this.items = items; }
}