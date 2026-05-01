package com.udeateampro.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@ExtendWith(MockitoExtension.class)
class OpenAPIConfigTest {

    @InjectMocks
    private OpenAPIConfig openAPIConfig;

    @Test
    void testCustomOpenAPIBeanCreation() {
        // When
        OpenAPI openAPI = openAPIConfig.customOpenAPI();

        // Then
        assertNotNull(openAPI);
    }

    @Test
    void testCustomOpenAPIInfo() {
        // When
        OpenAPI openAPI = openAPIConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Then
        assertNotNull(info);
        assertEquals("CotSys API", info.getTitle());
        assertEquals("v1", info.getVersion());
        assertTrue(info.getDescription().contains("CotSys"));
    }

    @Test
    void testCustomOpenAPISecurityScheme() {
        // When
        OpenAPI openAPI = openAPIConfig.customOpenAPI();

        // Then
        assertNotNull(openAPI);
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("bearerAuth"));
    }

    @Test
    void testCustomOpenAPISecurityRequirement() {
        // When
        OpenAPI openAPI = openAPIConfig.customOpenAPI();

        // Then
        assertNotNull(openAPI);
        assertTrue(openAPI.getSecurity().size() > 0);
        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        assertTrue(securityRequirement.containsKey("bearerAuth"));
    }

    @Test
    void testCustomOpenAPIBearerAuthType() {
        // When
        OpenAPI openAPI = openAPIConfig.customOpenAPI();

        // Then
        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("bearerAuth"));
        assertEquals("bearer", openAPI.getComponents().getSecuritySchemes().get("bearerAuth").getScheme());
        assertEquals("JWT", openAPI.getComponents().getSecuritySchemes().get("bearerAuth").getBearerFormat());
    }
}
