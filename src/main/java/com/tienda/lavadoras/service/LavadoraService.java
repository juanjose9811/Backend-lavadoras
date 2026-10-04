package com.tienda.lavadoras.service;

import com.tienda.lavadoras.model.Lavadora;
import com.tienda.lavadoras.repository.LavadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LavadoraService {

    @Autowired
    private LavadoraRepository lavadoraRepository;

    public List<Lavadora> obtenerTodas() {
        return lavadoraRepository.findAll();
    }

    public Optional<Lavadora> buscarPorId(Long id) {
        return lavadoraRepository.findById(id);
    }

    public Lavadora guardar(Lavadora lavadora) {
        return lavadoraRepository.save(lavadora);
    }

    public void eliminar(Long id) {
        lavadoraRepository.deleteById(id);
    }
}