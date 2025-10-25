package com.udeateampro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.udeateampro.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
