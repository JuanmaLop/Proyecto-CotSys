package com.udeateampro.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.udeateampro.entity.KitSolucion;

@Repository
public interface KitSolucionRepository extends JpaRepository<KitSolucion, Long>{
}
