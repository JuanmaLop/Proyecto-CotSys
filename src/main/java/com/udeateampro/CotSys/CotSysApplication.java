package com.udeateampro.CotSys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Ampliamos el escaneo de componentes y declaramos paquetes de entidades y repositorios
@SpringBootApplication(scanBasePackages = "com.udeateampro")
@EntityScan(basePackages = {"com.udeateampro.entity"})
@EnableJpaRepositories(basePackages = {"com.udeateampro.repository"})
public class CotSysApplication {

	public static void main(String[] args) {
		SpringApplication.run(CotSysApplication.class, args);
	}

}
