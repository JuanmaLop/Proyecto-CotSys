Feature: Gestión de Cotizaciones (CRUD protegido por rol)

  # Propósito: Validar que solo usuarios con rol ADMINISTRADOR o COMERCIAL
  # puedan crear, leer, actualizar y eliminar cotizaciones

  Scenario: Obtener lista de cotizaciones con token válido (Admin)
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
    
    # WHEN: Consulta GET /api/cotizaciones/get-all-cotizaciones
    # THEN: Retorna lista de cotizaciones (200 OK)
    Given url baseUrl
    And path '/api/cotizaciones/get-all-cotizaciones'
    And header Authorization = 'Bearer ' + adminToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Obtener lista de cotizaciones con token válido (Comercial)
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
    # WHEN: Consulta GET /api/cotizaciones/get-all-cotizaciones
    # THEN: Retorna lista de cotizaciones (200 OK)
    Given url baseUrl
    And path '/api/cotizaciones/get-all-cotizaciones'
    And header Authorization = 'Bearer ' + comercialToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Ciclo completo: Crear, Actualizar y Eliminar cotizacion
    # GIVEN: Admin autenticado para crear, actualizar y eliminar
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
    
    # PASO 1: Crear nueva cotización
    # THEN: Retorna 200 OK con la cotización creada
    Given url baseUrl
    And path '/api/cotizaciones/create-cotizacion'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "estado": "Borrador",
        "usuario": 11,
        "cliente": 7,
        "fechaCreacion": "2026-03-08",
        "fechaValidez": "2026-04-08",
        "margenGeneral": 10.00,
        "monedaCotizacion": "USD"
      }
      """
    When method post
    Then status 200
    And match response.estado == 'Borrador'
    And match response.monedaCotizacion == 'USD'
    * def cotizacionId = response.id
    And match cotizacionId == '#number'
    
    # PASO 2: Actualizar cotización con el ID creado
    # THEN: Retorna 200 OK con cotización actualizada
    Given url baseUrl
    And path '/api/cotizaciones/' + cotizacionId + '/update-cotizacion'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "estado": "Enviado",
        "usuario": 11,
        "cliente": 8,
        "fechaCreacion": "2026-03-08",
        "fechaValidez": "2026-05-08",
        "margenGeneral": 15.00,
        "monedaCotizacion": "USD"
      }
      """
    When method put
    Then status 200
    And match response.estado == 'Enviado'
    
    # PASO 3: Eliminar cotización con el mismo ID
    # THEN: Retorna 204 No Content
    Given url baseUrl
    And path '/api/cotizaciones/' + cotizacionId + '/delete-cotizacion'
    And header Authorization = 'Bearer ' + adminToken
    When method delete
    Then status 204

  Scenario: Obtener cotizaciones sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta GET /api/cotizaciones/get-all-cotizaciones
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/cotizaciones/get-all-cotizaciones'
    When method get
    Then status 403

  Scenario: Crear cotizacion con rol insuficiente retorna error
    # Obtener token de Técnico (no tiene permiso para crear cotizaciones)
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
    
    # GIVEN: Token de usuario que NO es ADMIN ni COMERCIAL
    # WHEN: Intenta POST /api/cotizaciones/create-cotizacion
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/cotizaciones/create-cotizacion'
    And header Authorization = 'Bearer ' + tecnicoToken
    And request
      """
      {
        "estado": "Borrador",
        "usuario": 11,
        "cliente": 7,
        "fechaCreacion": "2026-03-08",
        "fechaValidez": "2026-04-08",
        "margenGeneral": 10.00,
        "monedaCotizacion": "USD"
      }
      """
    When method post
    Then status 403
