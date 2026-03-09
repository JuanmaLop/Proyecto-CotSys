Feature: Gestión de Componentes Kit (CRUD protegido por rol)

  # Propósito: Validar que solo usuarios con rol ADMINISTRADOR o LIDER_TECNICO
  # puedan crear, leer, actualizar y eliminar componentes de kit

  Scenario: Obtener lista de componentes con token válido (Admin)
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
    
    # WHEN: Consulta GET /api/componente-kit/get-all
    # THEN: Retorna lista de componentes (200 OK)
    Given url baseUrl
    And path '/api/componente-kit/get-all'
    And header Authorization = 'Bearer ' + adminToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Obtener lista de componentes con token válido (Lider Tecnico)
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
    # WHEN: Consulta GET /api/componente-kit/get-all
    # THEN: Retorna lista de componentes (200 OK)
    Given url baseUrl
    And path '/api/componente-kit/get-all'
    And header Authorization = 'Bearer ' + tecnicoToken
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Ciclo completo: Crear, Actualizar y Eliminar componente
    # GIVEN: Admin autenticado para crear
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
    
    # PASO 1: Crear nuevo componente
    # THEN: Retorna 200 OK con el componente creado
    Given url baseUrl
    And path '/api/componente-kit/create-componente-kit'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "kitsolucion": 2,
        "producto": 11,
        "cantidad": 5,
        "instrucciones": "Montar en el área designada"
      }
      """
    When method post
    Then status 200
    And match response.cantidad == 5
    And match response.instrucciones == 'Montar en el área designada'
    * def componenteId = response.id_componente_kit
    
    # PASO 2: Actualizar componente con el ID creado
    # THEN: Retorna 200 OK con componente actualizado
    Given url baseUrl
    And path '/api/componente-kit/' + componenteId + '/update'
    And header Authorization = 'Bearer ' + adminToken
    And request
      """
      {
        "id_componente_kit": #(componenteId),
        "kitsolucion": 6,
        "producto": 22,
        "cantidad": 10,
        "instrucciones": "Montar con cuidado en el área designada"
      }
      """
    When method put
    Then status 200
    And match response.cantidad == 10
    
    # PASO 3: Eliminar componente con el mismo ID
    # THEN: Retorna 204 No Content
    Given url baseUrl
    And path '/api/componente-kit/' + componenteId + '/delete'
    And header Authorization = 'Bearer ' + adminToken
    When method delete
    Then status 204

  Scenario: Obtener componentes sin token retorna error 403
    # GIVEN: Sin token JWT
    # WHEN: Intenta GET /api/componente-kit/get-all
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/componente-kit/get-all'
    When method get
    Then status 403

  Scenario: Crear componente con rol insuficiente retorna error
    # Obtener token de Comercial (no tiene permiso para crear componentes)
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
    # WHEN: Intenta POST /api/componente-kit/create-componente-kit
    # THEN: Retorna 403 (Forbidden)
    Given url baseUrl
    And path '/api/componente-kit/create-componente-kit'
    And header Authorization = 'Bearer ' + comercialToken
    And request
      """
      {
        "kitsolucion": 1,
        "producto": 11,
        "cantidad": 5,
        "instrucciones": "Montar en el área designada"
      }
      """
    When method post
    Then status 403
