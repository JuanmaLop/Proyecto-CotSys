Feature: Gestión de Kits Soluciones (CRUD protegido por rol)

  # Propósito: Validar que solo usuarios con rol ADMINISTRADOR o LIDER_TECNICO
  # puedan crear, leer, actualizar y eliminar kits de soluciones

  Scenario: Obtener lista de kits con token válido (Admin)
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
    
    # WHEN: Consulta GET /api/kits/get-all-kits
    # THEN: Retorna lista de kits (200 OK)
    Given url baseUrl
    And path '/api/kits/get-all-kits'
    And header Authorization = 'Bearer ' + adminToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Obtener lista de kits con token válido (Lider Tecnico)
    # Obtener token de técnico
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
    
    # GIVEN: Usuario Líder Técnico autenticado
    # WHEN: Consulta GET /api/kits/get-all-kits
    # THEN: Retorna lista de kits (200 OK)
    Given url baseUrl
    And path '/api/kits/get-all-kits'
    And header Authorization = 'Bearer ' + tecnicoToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Ciclo completo: Crear, Actualizar y Eliminar kit
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
    
    # PASO 1: Crear nuevo kit
    Given url baseUrl
    And path '/api/kits/create-kit'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "nombre": "Kit Instalación Básica",
        "descripcion": "Kit completo para instalación básica de sistemas",
        "estado": true,
        "componentes": [
          {
            "id_producto": 11,
            "cantidad": 2,
            "instrucciones": "Instalar en orden",
            "estado": true
          },
          {
            "id_producto": 17,
            "cantidad": 1,
            "instrucciones": "Calibrar después",
            "estado": true
          }
        ]
      }
      """
    When method post
    Then status 200
    And match response.nombre == 'Kit Instalación Básica'
    And match response.estado == true
    * def kitId = response.id_kit

    # PASO 2: Actualizar kit con el ID creado
    Given url baseUrl
    And path '/api/kits/' + kitId + '/update-kit'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "nombre": "Kit Instalación Avanzada",
        "descripcion": "Kit completo para instalación avanzada de sistemas",
        "estado": true,
        "componentes": [
          {
            "id_producto": 21,
            "cantidad": 3,
            "instrucciones": "Instalar en orden específico",
            "estado": true
          }
        ]
      }
      """
    When method put
    Then status 200
    And match response.nombre == 'Kit Instalación Avanzada'

    # PASO 3: Eliminar kit con el mismo ID
    Given url baseUrl
    And path '/api/kits/' + kitId + '/delete-kit'
    And header Authorization = 'Bearer ' + adminToken
    When method delete
    Then status 204

  Scenario: Obtener kits sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta GET /api/kits/get-all-kits
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/kits/get-all-kits'
    When method get
    Then status 403

  Scenario: Crear kit con rol insuficiente retorna error
    # Obtener token de Comercial (no tiene permiso para crear kits)
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
    # WHEN: Intenta POST /api/kits/create-kit
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/kits/create-kit'
    And header Authorization = 'Bearer ' + comercialToken
    And request
      """
      {
        "nombre": "Kit No Autorizado",
        "descripcion": "Este kit no debe crearse",
        "estado": true,
        "componentes": []
      }
      """
    When method post
    Then status 403
