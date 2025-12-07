package com.udeateampro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udeateampro.entity.ComponenteKit;

@Repository
public interface ComponenteKitRepository extends JpaRepository<ComponenteKit, Long>  {
    List<ComponenteKit> findByKitSolucion(Long kitSolucion);
    
    void deleteByKitSolucion(Long kitSolucion);
}
