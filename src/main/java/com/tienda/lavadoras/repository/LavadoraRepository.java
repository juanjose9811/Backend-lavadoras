package com.tienda.lavadoras.repository;

import com.tienda.lavadoras.model.Lavadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LavadoraRepository extends JpaRepository<Lavadora, Long> {

    @Query("SELECT l FROM Lavadora l WHERE " +
            "(:texto IS NULL OR LOWER(l.marca) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(l.modelo) LIKE LOWER(CONCAT('%', :texto, '%'))) AND " +
            "(:precioMax IS NULL OR l.precio <= :precioMax) AND " +
            "(:capacidadMin IS NULL OR l.capacidad >= :capacidadMin)")
    List<Lavadora> buscarConFiltros(@Param("texto") String texto,
                                    @Param("precioMax") Double precioMax,
                                    @Param("capacidadMin") Double capacidadMin);
}
