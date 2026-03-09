Feature: Gestión de Usuarios (Protegida por rol ADMINISTRADOR)

  # Propósito: Validar que solo usuarios con rol ADMINISTRADOR
  # puedan leer y actualizar información de usuarios

  Scenario: Obtener lista de usuarios (Admin)
    # GIVEN: Admin autenticado
    * configure retry = { count: 6, interval: 300 }
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "adminprueba@inst.com",
        "password": "Password123"
      }
      """
    And retry until responseStatus == 200
    When method post
    Then status 200
    * def adminToken = response.access_token
    
    # WHEN: Consulta GET /api/users/get-all-users
    # THEN: Retorna lista de usuarios (200 OK)
    Given url baseUrl
    And path '/api/users/get-all-users'
    And header Authorization = 'Bearer ' + adminToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Ciclo completo: Crear y Actualizar usuario
    # GIVEN: Admin autenticado
    * configure retry = { count: 6, interval: 300 }
    Given url baseUrl
    And path '/api/auth/login'
    And request
      """
      {
        "email": "adminprueba@inst.com",
        "password": "Password123"
      }
      """
    And retry until responseStatus == 200
    When method post
    Then status 200
    * def adminToken = response.access_token
    
    # PASO 1: Crear nuevo usuario
    Given url baseUrl
    And path '/api/auth/create-user'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "nombre": "El coyote",
        "email": "elcoyotedetrasdelcorrecaminos123@inst.com",
        "rol": "Comercial",
        "password": "Password123"
      }
      """
    When method post
    Then status 200
    * def userEmail = response.email

    # PASO 2: Actualizar usuario creado
    Given url baseUrl
    And path '/api/users/update-user'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      [
        {
          "email": "#(userEmail)",
          "rol": "Administrador",
          "estado": true
        }
      ]
      """
    When method post
    Then status 200
    And match response == '#[]'

  Scenario: Obtener usuarios sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta GET /api/users/get-all-users
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/users/get-all-users'
    When method get
    Then status 403

  Scenario: Actualizar usuarios sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta POST /api/users/update-user
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/users/update-user'
    And request
      """
      [
        {
          "email": "comercialprueba@inst.com",
          "rol": "COMERCIAL",
          "estado": true
        }
      ]
      """
    When method post
    Then status 403

  Scenario: Obtener usuarios con rol insuficiente retorna error
    # Obtener token de Comercial (no tiene permiso para ver usuarios)
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
    * def comercialToken = response.access_token
    
    # GIVEN: Token de usuario que NO es ADMIN
    # WHEN: Intenta GET /api/users/get-all-users
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/users/get-all-users'
    And header Authorization = 'Bearer ' + comercialToken
    When method get
    Then status 403

  Scenario: Actualizar usuarios con rol insuficiente retorna error
    # Obtener token de Técnico (no tiene permiso para actualizar usuarios)
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
    * def tecnicoToken = response.access_token
    
    # GIVEN: Token de usuario que NO es ADMIN
    # WHEN: Intenta POST /api/users/update-user
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/users/update-user'
    And header Authorization = 'Bearer ' + tecnicoToken
    And request
      """
      [
        {
          "email": "comercialprueba@inst.com",
          "rol": "COMERCIAL",
          "estado": true
        }
      ]
      """
    When method post
    Then status 403
