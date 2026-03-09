package com.udeateampro.CotSys.Acceptance;

import com.intuit.karate.junit5.Karate;

/**
 * Runner de Acceptance Tests usando Karate (BDD con Gherkin/Cucumber).
 * 
 * Esta clase ejecuta TODOS los archivos .feature bajo src/test/resources/features/
 * Karate busca automáticamente:
 * - features/auth/login.feature
 * - features/productos/producto.feature
 * - features/clientes/clientes.feature
 * 
 * Ejecución: mvn -Dtest=AcceptanceTestRunner test
 */
public class AcceptanceTestRunner {

    @Karate.Test
    Karate runAll() {
        return Karate.run("classpath:features").relativeTo(getClass());
    }
}