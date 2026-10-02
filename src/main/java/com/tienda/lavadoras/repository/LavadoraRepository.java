package com.tienda.lavadoras.repository;

import com.tienda.lavadoras.model.Lavadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LavadoraRepository extends JpaRepository<Lavadora, Long> {
}

