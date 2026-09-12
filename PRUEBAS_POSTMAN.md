# Guía de Pruebas Postman — Lubrimax Microservicios

> **Prerrequisitos**  
> 1. MySQL 8 corriendo en `localhost:3306` con usuario `root` / contraseña `adminMILDER1234@`.  
> 2. Los ocho microservicios levantados en sus respectivos puertos.  
> 3. Importar las peticiones en Postman o ejecutarlas en el orden indicado (muchas dependen de IDs generados por pasos anteriores).

---

## Mapa de puertos

| Microservicio             | Puerto |
|--------------------------|--------|
| msvc-inventario           | 8010   |
| msvc-abastecimiento       | 8011   |
| msvc-proveedor            | 8012   |
| msvc-ejecucion-mantenimiento | 8051 |
| msvc-agenda-tecnico       | 8052   |
| msvc-acopio-temporal      | 8053   |
| msvc-entrega-operador     | 8054   |
| msvc-operador-autorizado  | 8055   |

---

## 1. msvc-proveedor (puerto 8012)

Base URL: `http://localhost:8012/api/proveedores`

### 1.1 Registrar proveedor
```
POST http://localhost:8012/api/proveedores
Content-Type: application/json

{
  "ruc": "20123456789",
  "razonSocial": "Lubricantes SAC",
  "contacto": "comercial@lubricantes.com",
  "activo": true
}
```
**Respuesta esperada:** `201 Created` con el proveedor creado (incluye `id`).  
**Guardar:** `proveedorId = <id del response>`

---

### 1.2 Listar proveedores
```
GET http://localhost:8012/api/proveedores
```
**Respuesta esperada:** `200 OK` con lista de proveedores.

---

### 1.3 Buscar proveedor por ID
```
GET http://localhost:8012/api/proveedores/{{proveedorId}}
```
**Respuesta esperada:** `200 OK` con el proveedor.

---

### 1.4 Actualizar proveedor
```
PUT http://localhost:8012/api/proveedores/{{proveedorId}}
Content-Type: application/json

{
  "ruc": "20123456789",
  "razonSocial": "Lubricantes SAC Actualizado",
  "contacto": "ventas@lubricantes.com",
  "activo": true
}
```
**Respuesta esperada:** `200 OK` con proveedor actualizado.

---

### 1.5 Eliminar proveedor (sin recepciones)
```
DELETE http://localhost:8012/api/proveedores/{{proveedorId}}
```
**Respuesta esperada:** `204 No Content`.  
**Nota:** Fallará con `400` si el proveedor tiene recepciones de mercadería registradas.

---

## 2. msvc-inventario (puerto 8010)

Base URL: `http://localhost:8010/api/inventario`

### 2.1 Listar existencias
```
GET http://localhost:8010/api/inventario
```
**Respuesta esperada:** `200 OK` con lista vacía inicialmente.

---

### 2.2 Habilitar stock (lo llama msvc-abastecimiento al cerrar recepción)
```
POST http://localhost:8010/api/inventario/habilitar?productoId=1&presentacionId=1&cantidad=50&recepcionId=1
```
**Respuesta esperada:** `201 Created` con la existencia creada.  
**Guardar:** `existenciaId = <id del response>`

---

### 2.3 Buscar existencia por ID
```
GET http://localhost:8010/api/inventario/{{existenciaId}}
```
**Respuesta esperada:** `200 OK` con la existencia.

---

### 2.4 Buscar por producto y presentación
```
GET http://localhost:8010/api/inventario/producto/1/presentacion/1
```
**Respuesta esperada:** `200 OK` con la existencia.

---

### 2.5 Reservar stock
```
PUT http://localhost:8010/api/inventario/{{existenciaId}}/reservar?orden=101&cantidad=5
```
**Respuesta esperada:** `200 OK` con saldo actualizado (reservado incrementado).

---

### 2.6 Confirmar reserva
```
PUT http://localhost:8010/api/inventario/{{existenciaId}}/confirmar-reserva?orden=101
```
**Respuesta esperada:** `200 OK`.

---

### 2.7 Liberar reserva
```
PUT http://localhost:8010/api/inventario/{{existenciaId}}/liberar-reserva?orden=101
```
**Respuesta esperada:** `200 OK`.

---

### 2.8 Consumir stock (tras confirmar reserva)
```
PUT http://localhost:8010/api/inventario/{{existenciaId}}/consumir?orden=101&cantidad=2
```
**Respuesta esperada:** `200 OK` con saldo reducido.

---

### 2.9 Eliminar existencia (solo si saldo = 0)
```
DELETE http://localhost:8010/api/inventario/{{existenciaId}}
```
**Respuesta esperada:** `204 No Content` si el saldo físico y reservado son cero.

---

## 3. msvc-abastecimiento (puerto 8011)

Base URL: `http://localhost:8011/api/abastecimiento`  
**Requiere:** msvc-proveedor (8012) e msvc-inventario (8010) levantados.

### 3.1 Listar recepciones
```
GET http://localhost:8011/api/abastecimiento
```
**Respuesta esperada:** `200 OK`.

---

### 3.2 Iniciar recepción de mercadería
```
POST http://localhost:8011/api/abastecimiento/iniciar?proveedorId={{proveedorId}}&documento=FAC-001-00001234
```
**Respuesta esperada:** `201 Created` con la recepción creada (estado `PENDIENTE`).  
**Guardar:** `recepcionId = <id del response>`

---

### 3.3 Buscar recepción por ID
```
GET http://localhost:8011/api/abastecimiento/{{recepcionId}}
```
**Respuesta esperada:** `200 OK`.

---

### 3.4 Agregar línea verificada
```
POST http://localhost:8011/api/abastecimiento/{{recepcionId}}/lineas?presentacion=1&cantidad=50&costo=25.50&lote=LOTE-2024-001&verificacion=CONFORME&productoId=1
```
**Valores de `verificacion`:** `CONFORME`, `NO_CONFORME`, `PENDIENTE`  
**Respuesta esperada:** `200 OK` con la recepción actualizada.  
**Guardar:** `lineaId = <id de la línea en el response>`

---

### 3.5 Registrar observación en línea
```
POST http://localhost:8011/api/abastecimiento/{{recepcionId}}/observaciones?lineaId={{lineaId}}&condicion=BUEN_ESTADO
```
**Valores de `condicion`:** `BUEN_ESTADO`, `DANADO`, `VENCIDO`  
**Respuesta esperada:** `200 OK`.

---

### 3.6 Dar conformidad a la recepción
```
PUT http://localhost:8011/api/abastecimiento/{{recepcionId}}/conformar
```
**Respuesta esperada:** `200 OK` con estado cambiado a `CONFORME`.

---

### 3.7 Cerrar recepción (sincroniza stock con msvc-inventario)
```
PUT http://localhost:8011/api/abastecimiento/{{recepcionId}}/cerrar
```
**Respuesta esperada:** `200 OK` con estado `CERRADO`.  
**Efecto secundario:** Llama a `POST /api/inventario/habilitar` en msvc-inventario.

---

### 3.8 Verificar si proveedor tiene recepciones
```
GET http://localhost:8011/api/abastecimiento/existe-por-proveedor?proveedorId={{proveedorId}}
```
**Respuesta esperada:** `200 OK` con `true` o `false`.

---

## 4. msvc-operador-autorizado (puerto 8055)

Base URL: `http://localhost:8055/api/operadores`

### 4.1 Registrar operador autorizado
```
POST http://localhost:8055/api/operadores
Content-Type: application/json

{
  "ruc": "20456789012",
  "razonSocial": "Residuos Industriales EIRL",
  "telefono": "998877665",
  "registroEors": "EORS-2024-0042",
  "desde": "2024-01-01",
  "hasta": "2025-12-31"
}
```
**Respuesta esperada:** `201 Created`.  
**Guardar:** `operadorId = <id del response>`

---

### 4.2 Listar operadores
```
GET http://localhost:8055/api/operadores
```
**Respuesta esperada:** `200 OK`.

---

### 4.3 Obtener operador por ID
```
GET http://localhost:8055/api/operadores/{{operadorId}}
```
**Respuesta esperada:** `200 OK`.

---

### 4.4 Actualizar datos del operador
```
PUT http://localhost:8055/api/operadores/{{operadorId}}
Content-Type: application/json

{
  "razonSocial": "Residuos Industriales EIRL Actualizado",
  "telefono": "987654321"
}
```
**Respuesta esperada:** `200 OK`.

---

### 4.5 Actualizar autorización
```
PUT http://localhost:8055/api/operadores/{{operadorId}}/autorizacion
Content-Type: application/json

{
  "registroEors": "EORS-2025-0099",
  "desde": "2025-01-01",
  "hasta": "2026-12-31"
}
```
**Respuesta esperada:** `200 OK`.

---

### 4.6 Consultar vigencia del operador
```
GET http://localhost:8055/api/operadores/{{operadorId}}/vigencia?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK` con campo `vigente: true/false`.

---

### 4.7 Listar operadores vigentes
```
GET http://localhost:8055/api/operadores/vigentes?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK` con lista de operadores vigentes en esa fecha.

---

## 5. msvc-agenda-tecnico (puerto 8052)

Base URL: `http://localhost:8052/api/agendas`  
**Requiere:** msvc-administracion en `http://localhost:8060` (servicio externo para verificar técnicos).

### 5.1 Obtener agenda de un técnico
```
GET http://localhost:8052/api/agendas/{{usuarioId}}
```
**Respuesta esperada:** `200 OK` con la agenda (o `400` si no existe aún).

---

### 5.2 Asignar trabajo al técnico
```
POST http://localhost:8052/api/agendas/{{usuarioId}}/asignaciones
Content-Type: application/json

{
  "ordenId": 101,
  "inicio": "2025-06-15T08:00:00",
  "fin": "2025-06-15T12:00:00"
}
```
**Respuesta esperada:** `200 OK` con la asignación creada.  
**Guardar:** `asignacionId = <id de la asignación>`

---

### 5.3 Consultar disponibilidad
```
GET http://localhost:8052/api/agendas/{{usuarioId}}/disponibilidad?inicio=2025-06-15T08:00:00&fin=2025-06-15T12:00:00
```
**Respuesta esperada:** `200 OK` con `{ "disponible": true/false }`.

---

### 5.4 Iniciar asignación
```
POST http://localhost:8052/api/agendas/{{usuarioId}}/asignaciones/{{asignacionId}}/iniciar
```
**Respuesta esperada:** `200 OK` con asignación en estado `EN_CURSO`.

---

### 5.5 Liberar asignación
```
POST http://localhost:8052/api/agendas/{{usuarioId}}/asignaciones/{{asignacionId}}/liberar
```
**Respuesta esperada:** `200 OK` con asignación en estado `LIBERADA`.

---

### 5.6 Listar asignaciones del técnico
```
GET http://localhost:8052/api/agendas/{{usuarioId}}/asignaciones
```
**Respuesta esperada:** `200 OK` con lista de asignaciones.

---

### 5.7 Listar asignaciones por fecha
```
GET http://localhost:8052/api/agendas/asignaciones?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK`.

---

### 5.8 Validar asignación para una orden (usado por Feign desde ejecución)
```
GET http://localhost:8052/api/agendas/{{usuarioId}}/asignaciones/orden/101/valida
```
**Respuesta esperada:** `200 OK` con `{ "valida": true/false }`.

---

## 6. msvc-acopio-temporal (puerto 8053)

Base URL: `http://localhost:8053/api/acopios`

### 6.1 Crear acopio temporal
```
POST http://localhost:8053/api/acopios
Content-Type: application/json

{
  "tipoResiduo": "ACEITE_USADO",
  "capacidad": 500.0,
  "unidad": "LITROS"
}
```
**Valores de `tipoResiduo`:** `ACEITE_USADO`, `FILTROS_USADOS`, `BATERIAS_USADAS`  
**Respuesta esperada:** `201 Created`.

---

### 6.2 Obtener acopio por tipo
```
GET http://localhost:8053/api/acopios/ACEITE_USADO
```
**Respuesta esperada:** `200 OK`.

---

### 6.3 Configurar capacidad
```
PUT http://localhost:8053/api/acopios/ACEITE_USADO/capacidad
Content-Type: application/json

{
  "capacidad": 1000.0,
  "unidad": "LITROS"
}
```
**Respuesta esperada:** `200 OK`.

---

### 6.4 Consultar estado del acopio
```
GET http://localhost:8053/api/acopios/ACEITE_USADO/estado
```
**Respuesta esperada:** `200 OK` con `cantidadAcopiada`, `capacidadDisponible` y `condicion`.

---

### 6.5 Registrar residuo generado
```
POST http://localhost:8053/api/acopios/ACEITE_USADO/residuos
Content-Type: application/json

{
  "ordenId": 101,
  "declaracionOrigenId": 1,
  "cantidad": 3.5,
  "unidad": "LITROS",
  "fechaGeneracion": "2025-06-15T10:30:00"
}
```
**Respuesta esperada:** `201 Created`.  
**Guardar:** `residuoId = <id del response>`

---

### 6.6 Almacenar residuo (moverlo a ALMACENADO)
```
POST http://localhost:8053/api/acopios/ACEITE_USADO/residuos/{{residuoId}}/almacenar
```
**Respuesta esperada:** `200 OK`.

---

### 6.7 Listar residuos del acopio
```
GET http://localhost:8053/api/acopios/ACEITE_USADO/residuos
```
**Respuesta esperada:** `200 OK` con lista de residuos.

---

### 6.8 Obtener residuo específico
```
GET http://localhost:8053/api/acopios/ACEITE_USADO/residuos/{{residuoId}}
```
**Respuesta esperada:** `200 OK`.

---

### 6.9 Confirmar entrega de residuos
```
POST http://localhost:8053/api/acopios/ACEITE_USADO/entregas/confirmar
Content-Type: application/json

{
  "entregaId": 1,
  "residuosIds": [{{residuoId}}]
}
```
**Respuesta esperada:** `200 OK`.

---

## 7. msvc-ejecucion-mantenimiento (puerto 8051)

Base URL: `http://localhost:8051/api/ejecuciones`  
**Requiere:** msvc-agenda-tecnico (8052), msvc-acopio-temporal (8053) y msvc-orden-servicio (8061, servicio externo).

### 7.1 Crear ejecución de mantenimiento
```
POST http://localhost:8051/api/ejecuciones
Content-Type: application/json

{
  "ordenId": 101,
  "tecnicoUsuarioId": {{usuarioId}},
  "kilometraje": 45000
}
```
**Respuesta esperada:** `201 Created`.

---

### 7.2 Listar ejecuciones
```
GET http://localhost:8051/api/ejecuciones
```
**Respuesta esperada:** `200 OK`.

---

### 7.3 Obtener ejecución por ordenId
```
GET http://localhost:8051/api/ejecuciones/101
```
**Respuesta esperada:** `200 OK`.

---

### 7.4 Iniciar ejecución
```
POST http://localhost:8051/api/ejecuciones/101/iniciar
```
**Respuesta esperada:** `200 OK`.

---

### 7.5 Suspender ejecución
```
POST http://localhost:8051/api/ejecuciones/101/suspender
```
**Respuesta esperada:** `200 OK`.

---

### 7.6 Registrar tarea
```
POST http://localhost:8051/api/ejecuciones/101/tareas
Content-Type: application/json

{
  "servicioId": 1,
  "tipo": "CAMBIO_ACEITE"
}
```
**Valores de `tipo`:** `CAMBIO_ACEITE`, `CAMBIO_FILTRO`, `REVISION_GENERAL`, `OTRO`  
**Respuesta esperada:** `200 OK`.  
**Guardar:** `tareaId = <id de la tarea>`

---

### 7.7 Iniciar tarea
```
POST http://localhost:8051/api/ejecuciones/101/tareas/{{tareaId}}/iniciar
```
**Respuesta esperada:** `200 OK`.

---

### 7.8 Completar tarea
```
POST http://localhost:8051/api/ejecuciones/101/tareas/{{tareaId}}/completar
```
**Respuesta esperada:** `200 OK`.

---

### 7.9 Marcar tarea como no realizada
```
POST http://localhost:8051/api/ejecuciones/101/tareas/{{tareaId}}/no-realizada
```
**Respuesta esperada:** `200 OK`.

---

### 7.10 Registrar consumo de producto
```
POST http://localhost:8051/api/ejecuciones/101/consumos
Content-Type: application/json

{
  "productoId": 1,
  "cantidad": 4.5,
  "unidad": "LITROS"
}
```
**Respuesta esperada:** `200 OK`.

---

### 7.11 Declarar residuo generado
```
POST http://localhost:8051/api/ejecuciones/101/residuos
Content-Type: application/json

{
  "tareaId": {{tareaId}},
  "tipo": "ACEITE_USADO",
  "volumen": 3.5,
  "unidad": "LITROS"
}
```
**Valores de `tipo`:** `ACEITE_USADO`, `FILTROS_USADOS`, `BATERIAS_USADAS`  
**Respuesta esperada:** `200 OK`.

---

### 7.12 Registrar observación preventiva
```
POST http://localhost:8051/api/ejecuciones/101/observaciones
Content-Type: application/json

{
  "tipo": "DESGASTE_FRENOS",
  "descripcion": "Los frenos delanteros muestran desgaste avanzado"
}
```
**Respuesta esperada:** `200 OK`.

---

### 7.13 Calcular liquidación
```
POST http://localhost:8051/api/ejecuciones/101/liquidacion
```
**Respuesta esperada:** `200 OK` con el detalle de la liquidación.

---

### 7.14 Definir próximo servicio
```
POST http://localhost:8051/api/ejecuciones/101/proximo-servicio
Content-Type: application/json

{
  "fechaSugerida": "2025-09-15",
  "kilometrajeSugerido": 50000
}
```
**Respuesta esperada:** `200 OK`.

---

### 7.15 Cerrar ejecución
```
POST http://localhost:8051/api/ejecuciones/101/cerrar
```
**Respuesta esperada:** `200 OK` con estado `CERRADO`.

---

## 8. msvc-entrega-operador (puerto 8054)

Base URL: `http://localhost:8054/api/entregas`  
**Requiere:** msvc-operador-autorizado (8055) y msvc-acopio-temporal (8053) levantados.

### 8.1 Programar entrega a operador
```
POST http://localhost:8054/api/entregas
Content-Type: application/json

{
  "operadorId": {{operadorId}},
  "usuarioResponsableId": 1,
  "fechaEntrega": "2025-07-10"
}
```
**Respuesta esperada:** `201 Created`.  
**Guardar:** `entregaId = <id del response>`

---

### 8.2 Listar entregas
```
GET http://localhost:8054/api/entregas
```
**Respuesta esperada:** `200 OK`.

---

### 8.3 Obtener entrega por ID
```
GET http://localhost:8054/api/entregas/{{entregaId}}
```
**Respuesta esperada:** `200 OK`.

---

### 8.4 Agregar línea de residuos a la entrega
```
POST http://localhost:8054/api/entregas/{{entregaId}}/lineas
Content-Type: application/json

{
  "tipoResiduo": "ACEITE_USADO",
  "cantidad": 3.5,
  "unidad": "LITROS",
  "residuosIds": [{{residuoId}}]
}
```
**Valores de `tipoResiduo`:** `ACEITE_USADO`, `FILTROS_USADOS`, `BATERIAS_USADAS`  
**Respuesta esperada:** `200 OK`.  
**Guardar:** `lineaEntregaId = <id de la línea>`

---

### 8.5 Eliminar línea de la entrega
```
DELETE http://localhost:8054/api/entregas/{{entregaId}}/lineas/{{lineaEntregaId}}
```
**Respuesta esperada:** `200 OK`.

---

### 8.6 Registrar manifiesto de residuos
```
POST http://localhost:8054/api/entregas/{{entregaId}}/manifiesto
Content-Type: application/json

{
  "numero": "MAN-2025-0001",
  "fecha": "2025-07-10",
  "cantidadTotal": 3.5,
  "unidad": "LITROS",
  "responsableRecepcion": "Juan Pérez"
}
```
**Respuesta esperada:** `200 OK`.

---

### 8.7 Ejecutar entrega
```
POST http://localhost:8054/api/entregas/{{entregaId}}/ejecutar
```
**Respuesta esperada:** `200 OK`.

---

### 8.8 Conformar entrega
```
POST http://localhost:8054/api/entregas/{{entregaId}}/conformar
```
**Respuesta esperada:** `200 OK` con estado `CONFORME`.

---

## Flujo de integración completo recomendado

Ejecutar en este orden para demostrar la integración entre microservicios:

```
1. [msvc-proveedor]        POST /api/proveedores              → crea proveedor
2. [msvc-abastecimiento]   POST /api/abastecimiento/iniciar   → inicia recepción
3. [msvc-abastecimiento]   POST /api/abastecimiento/{id}/lineas → agrega línea
4. [msvc-abastecimiento]   PUT  /api/abastecimiento/{id}/conformar
5. [msvc-abastecimiento]   PUT  /api/abastecimiento/{id}/cerrar → sincroniza inventario
6. [msvc-inventario]       GET  /api/inventario               → confirma stock habilitado
7. [msvc-operador-autorizado] POST /api/operadores            → registra operador
8. [msvc-acopio-temporal]  POST /api/acopios                  → crea acopio ACEITE_USADO
9. [msvc-acopio-temporal]  POST /api/acopios/ACEITE_USADO/residuos → registra residuo
10. [msvc-acopio-temporal] POST /api/acopios/ACEITE_USADO/residuos/{id}/almacenar
11. [msvc-entrega-operador] POST /api/entregas                → programa entrega
12. [msvc-entrega-operador] POST /api/entregas/{id}/lineas    → agrega línea (residuoId)
13. [msvc-entrega-operador] POST /api/entregas/{id}/manifiesto
14. [msvc-entrega-operador] POST /api/entregas/{id}/ejecutar
15. [msvc-entrega-operador] POST /api/entregas/{id}/conformar
```

---

## Notas sobre dependencias externas

Los siguientes microservicios hacen llamadas Feign a servicios **no incluidos** en este repositorio:

| Microservicio            | Servicio externo        | URL configurada          |
|-------------------------|------------------------|--------------------------|
| msvc-agenda-tecnico     | msvc-administracion     | http://localhost:8060    |
| msvc-ejecucion-mantenimiento | msvc-orden-servicio | http://localhost:8061    |

Para probar estos microservicios de forma aislada, levanta un stub/mock en los puertos correspondientes que retorne las respuestas esperadas, o bien asegúrate de tener dichos servicios disponibles.

---

*Generado automáticamente — Lubrimax Ingeniería de Software II*
