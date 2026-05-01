package com.udeateampro;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.udeateampro.CotSys.CotSysApplication;

@SpringBootTest(classes = CotSysApplication.class)
class CotSysApplicationTest {

    @Test
    void contextLoads(ApplicationContext context) {
        // When & Then
        assertNotNull(context);
    }

    @Test
    void applicationContextCreated(ApplicationContext context) {
        // Then
        assertNotNull(context);
    }

    @Test
    void mainApplicationBeanExists(ApplicationContext context) {
        // Then
        assertNotNull(context);
        String[] beanNames = context.getBeanDefinitionNames();
        assertTrue(beanNames.length > 0);
    }
}
