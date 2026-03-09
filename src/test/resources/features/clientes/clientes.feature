Feature: Gestión de Clientes (CRUD protegido por rol)

  # Propósito: Validar que solo usuarios con rol ADMINISTRADOR o COMERCIAL
  # puedan crear, leer, actualizar y eliminar clientes

  Scenario: Obtener lista de clientes con token válido (Admin)
    # GIVEN: Admin autenticado
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
    
    # WHEN: Consulta GET /api/clientes/get-all-clientes
    # THEN: Retorna lista de clientes (200 OK)
    Given url baseUrl
    And path '/api/clientes/get-all-clientes'
    And header Authorization = 'Bearer ' + adminToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Obtener lista de clientes con token válido (Comercial)
    # Obtener token de comercial
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
    
    # GIVEN: Usuario Comercial autenticado
    # WHEN: Consulta GET /api/clientes/get-all-clientes
    # THEN: Retorna lista de clientes (200 OK)
    Given url baseUrl
    And path '/api/clientes/get-all-clientes'
    And header Authorization = 'Bearer ' + comercialToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Ciclo completo: Crear, Actualizar y Eliminar cliente
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
    
    # PASO 1: Crear nuevo cliente
    Given url baseUrl
    And path '/api/clientes/create-cliente'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "nombre": "Acme Corp",
        "nit": "100455543",
        "direccion": "Fairfield",
        "autorrentenedor": false,
        "municipio": "Nueva Jerse", 
        "tipoRegimen": "Común"
      }
      """
    When method post
    Then status 200
    * def clienteId = response.id_cliente
    
    # PASO 2: Actualizar cliente con el ID creado
    Given url baseUrl
    And path '/api/clientes/' + clienteId + '/update-cliente'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "nombre": "Acme Corp Actualizada",
        "nit": "100455543",
        "direccion": "Fairlife",
        "autorrentenedor": true,
        "municipio": "Nueva Jerse", 
        "tipoRegimen": "Especial"
      }
      """
    When method put
    Then status 200
    
    # PASO 3: Eliminar cliente con el mismo ID
    Given url baseUrl
    And path '/api/clientes/' + clienteId + '/delete-cliente'
    And header Authorization = 'Bearer ' + adminToken
    When method delete
    Then status 204

  Scenario: Obtener clientes sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta GET /api/clientes/get-all-clientes
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/clientes/get-all-clientes'
    When method get
    Then status 403

  Scenario: Crear cliente con rol insuficiente (Técnico) retorna error
    # Obtener token de técnico (no tiene permiso para crear clientes)
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
    
    # GIVEN: Token de LIDER_TECNICO (no tiene permiso para crear clientes)
    # WHEN: Intenta POST /api/clientes/create-cliente
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/clientes/create-cliente'
    And header Authorization = 'Bearer ' + tecnicoToken
    And request
      """
      {
        "nombre": "ArreglenPortalUniversitario Corp",
        "nit": "1004523443",
        "direccion": "Calle 19 #10-54",
        "autorrentenedor": false,
        "municipio": "Medellín", 
        "tipoRegimen": "Común"
      }
      """
    When method post
    Then status 403


