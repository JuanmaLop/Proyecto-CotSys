package com.udeateampro.CotSys.repository;

import com.udeateampro.CotSys.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductoRepository extends JpaRepository<Producto, String> {

}
