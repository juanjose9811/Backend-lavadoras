package com.tienda.lavadoras.controller;

import com.tienda.lavadoras.exception.RecursoNoEncontradoException;
import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.repository.LavadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/lavadoras")
public class LavadoraController {

    @Autowired
    private LavadoraRepository repository;

    @GetMapping
    public List<Lavadora> listar() {
        return repository.findAll();
    }

    @GetMapping("/buscar")
    public List<Lavadora> buscar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Double capacidadMin) {

        String textoLimpio = (texto != null && !texto.trim().isEmpty()) ? texto.trim() : null;
        return repository.buscarConFiltros(textoLimpio, precioMax, capacidadMin);
    }

    @PostMapping
    public Lavadora guardar(@Valid @RequestBody Lavadora lavadora) {
        return repository.save(lavadora);
    }

    @PutMapping("/{id}")
    public Lavadora actualizar(@PathVariable Long id, @Valid @RequestBody Lavadora nueva) {
        return repository.findById(id)
                .map(l -> {
                    l.setMarca(nueva.getMarca());
                    l.setModelo(nueva.getModelo());
                    l.setPrecio(nueva.getPrecio());
                    l.setStock(nueva.getStock()); //
                    l.setCapacidad(nueva.getCapacidad());
                    l.setImagenUrl(nueva.getImagenUrl());
                    return repository.save(l);
                })
                .orElseThrow(() -> new RecursoNoEncontradoException("Lavadora no encontrada con id: " + id));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}