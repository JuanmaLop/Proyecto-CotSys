Feature: Autenticación y Login en CotSys

  # Propósito: Validar que la API autentica correctamente usuarios y genera tokens JWT válidos

  Scenario: Login exitoso con usuario Admin
    # GIVEN: Una URL y endpoint de login
    # WHEN: Se envía email y password correctos
    # THEN: Se retorna acceso exitoso (200) con tokens JWT
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "adminprueba@inst.com",
        "password": "Password123"
      }
      """
    When method post
    Then status 200
    And match response.access_token == '#string'
    And match response.refresh_token == '#string'
    * def result = { accessToken: response.access_token, refreshToken: response.refresh_token }

  Scenario: Login exitoso con usuario Comercial
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "comercialprueba@inst.com",
        "password": "Password123"
      }
      """
    When method post
    Then status 200
    And match response.access_token == '#string'
    And match response.refresh_token == '#string'
    * def result = { accessToken: response.access_token, refreshToken: response.refresh_token }

  Scenario: Login exitoso Técnico
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "usuarioprueba@inst.com",
        "password": "Password123"
      }
      """
    When method post
    Then status 200
    And match response.access_token == '#string'
    And match response.refresh_token == '#string'
    * def result = { accessToken: response.access_token, refreshToken: response.refresh_token }

  Scenario: Login fallido con contraseña incorrecta
    # WHEN: Se envía contraseña incorrecta
    # THEN: Se rechaza con código 403 (Forbidden)
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "adminprueba@inst.com",
        "password": "Password1010"
      }
      """
    When method post
    Then status 403

  Scenario: Login fallido con usuario inexistente
    # WHEN: Se envía un email que no existe
    # THEN: Se rechaza con código 403
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "hackerintrusivo@inst.com",
        "password": "Password123"
      }
      """
    When method post
    Then status 403