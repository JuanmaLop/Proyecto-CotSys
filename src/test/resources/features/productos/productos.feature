Feature: Gestión de Productos (CRUD protegido por rol)

  # Propósito: Validar que solo usuarios con rol ADMINISTRADOR o LIDER_TECNICO
  # puedan crear, leer, actualizar y eliminar productos

  Scenario: Obtener lista de productos con token válido (Admin)
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
    
    # WHEN: Consulta GET /api/productos/get-all-productos
    # THEN: Retorna lista de productos (200 OK)
    Given url baseUrl
    And path '/api/productos/get-all-productos'
    And header Authorization = 'Bearer ' + adminToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Crear nuevo producto (Admin)
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
    
    # WHEN: POST nuevo producto con todos los campos requeridos
    # THEN: Retorna 200 OK con el producto creado
    Given url baseUrl
    And path '/api/productos/create-producto'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "nombre": "Cohete ACME",
        "descripcion": "Cohete compacto para entretenimiento",
        "categoria": "Entretenimiento",
        "unidadMedida": "Unidad",
        "costoBase": 8500,
        "monedaOriginal": "USD",
        "tipo": "Producto"
      }
      """
    When method post
    Then status 200
    And match response.nombre == 'Cohete ACME'
    And match response.categoria == 'Entretenimiento'
    * def productoId = response.id_producto

  Scenario: Obtener productos sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta GET /api/productos/get-all-productos
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/productos/get-all-productos'
    When method get
    Then status 403

  Scenario: Crear producto con rol insuficiente retorna error
    # Obtener token de Comercial (no tiene permiso para crear productos)
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
    
    # GIVEN: Token de usuario que NO es ADMIN ni LIDER_TECNICO
    # WHEN: Intenta POST /api/productos/create-producto
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/productos/create-producto'
    And header Authorization = 'Bearer ' + comercialToken
    And request
      """
      {
        "nombre": "Cohete Ajax Corporation",
        "descripcion": "Cohete compacto para entretenimiento (Mejor que ACME)",
        "categoria": "Entretenimiento",
        "unidadMedida": "Unidad",
        "costoBase": 7500,
        "monedaOriginal": "USD",
        "tipo": "Producto"
      }
      """
    When method post
    Then status 403