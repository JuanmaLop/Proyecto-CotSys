package com.udeateampro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.udeateampro.entity.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

}
