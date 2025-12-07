package com.udeateampro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udeateampro.entity.Impuesto;

import java.util.List;

@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {
    List<Impuesto> findByCotizacion(Long cotizacion);
}
