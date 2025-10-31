package com.udeateampro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jwt_token")
public class JwtToken {

    public enum TokenType {
        BEARER,
        REFRESH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long id;

    @Column(name = "token", nullable = false, unique = true, length = 1000)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_token", nullable = false)
    private TokenType tipoToken;

    @Column(name = "revocado", nullable = false)
    private boolean revocado;

    @Column(name = "expirado", nullable = false)
    private boolean expirado;

    @Column(name = "usuario", nullable = false)
    private Long usuario;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(TokenType tipoToken) {
        this.tipoToken = tipoToken;
    }

    public boolean isRevocado() {
        return revocado;
    }

    public void setRevocado(boolean revocado) {
        this.revocado = revocado;
    }

    public boolean isExpirado() {
        return expirado;
    }  

    public void setExpirado(boolean expirado) {
        this.expirado = expirado;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

}
