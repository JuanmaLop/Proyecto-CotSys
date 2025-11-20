package com.udeateampro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udeateampro.entity.ComponenteKit;

@Repository
public interface ComponenteKitRepository extends JpaRepository<ComponenteKit, Long>  {

}
