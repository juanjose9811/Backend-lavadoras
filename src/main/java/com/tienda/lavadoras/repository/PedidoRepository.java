package com.tienda.lavadoras.repository;

import com.tienda.lavadoras.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteUsernameOrderByIdDesc(String clienteUsername);
}