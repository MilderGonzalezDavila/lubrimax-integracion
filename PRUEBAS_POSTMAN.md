# Guía de Pruebas Postman — Lubrimax Microservicios

> **Prerrequisitos**
> 1. MySQL 8 corriendo en `localhost:3306` con usuario `root` / contraseña `adminMILDER1234@`.
> 2. Los quince microservicios levantados en sus respectivos puertos.
> 3. Importar las peticiones en Postman o ejecutarlas en el orden indicado (muchas dependen de IDs generados por pasos anteriores).

---

## Mapa de puertos

| # | Microservicio                  | Puerto | Base de datos                            |
|---|-------------------------------|--------|------------------------------------------|
| 1 | msvc-usuarios                 | 8001   | msvc_lubrimax_usuarios                   |
| 2 | msvc-politicas                | 8002   | msvc_lubrimax_politicas                  |
| 3 | msvc-auditorias               | 8003   | msvc_lubrimax_auditorias                 |
| 4 | msvc-clientes                 | 8004   | msvc_lubrimax_clientes                   |
| 5 | msvc-proveedor                | 8005   | msvc_lubrimax_proveedor                  |
| 6 | msvc-abastecimiento           | 8006   | msvc_lubrimax_abastecimiento             |
| 7 | msvc-inventario               | 8007   | msvc_lubrimax_inventario                 |
| 8 | msvc-vehiculos                | 8008   | msvc_lubrimax_vehiculos                  |
| 9 | msvc-historial-mantenimiento  | 8009   | msvc_lubrimax_historial_mantenimiento    |
|10 | msvc-orden-servicio           | 8010   | msvc_lubrimax_orden_servicio             |
|11 | msvc-ejecucion-mantenimiento  | 8011   | msvc_lubrimax_ejecucion_mantenimiento    |
|12 | msvc-agenda-tecnico           | 8012   | msvc_lubrimax_agenda_tecnico             |
|13 | msvc-acopio-temporal          | 8013   | msvc_lubrimax_acopio_temporal            |
|14 | msvc-entrega-operador         | 8014   | msvc_lubrimax_entrega_operador           |
|15 | msvc-operador-autorizado      | 8015   | msvc_lubrimax_operador_autorizado        |

---

## 1. msvc-usuarios (puerto 8001)

Base URL: `http://localhost:8001/api/usuarios`

### 1.1 Crear usuario
```
POST http://localhost:8001/api/usuarios
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan.perez@lubrimax.com",
  "rol": "TECNICO"
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `usuarioId = <id del response>`

---

### 1.2 Listar usuarios
```
GET http://localhost:8001/api/usuarios
```
**Respuesta esperada:** `200 OK`.

---

### 1.3 Buscar usuario por ID
```
GET http://localhost:8001/api/usuarios/{{usuarioId}}
```
**Respuesta esperada:** `200 OK`.

---

### 1.4 Actualizar usuario
```
PUT http://localhost:8001/api/usuarios/{{usuarioId}}
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "email": "juan.actualizado@lubrimax.com",
  "rol": "TECNICO"
}
```
**Respuesta esperada:** `200 OK`.

---

### 1.5 Eliminar usuario
```
DELETE http://localhost:8001/api/usuarios/{{usuarioId}}
```
**Respuesta esperada:** `204 No Content`.

---

## 2. msvc-politicas (puerto 8002)

Base URL: `http://localhost:8002/api/politicas`

### 2.1 Crear política del negocio
```
POST http://localhost:8002/api/politicas
Content-Type: application/json

{
  "nombre": "Política de Cambio de Aceite",
  "descripcion": "Cambio cada 5000 km o 3 meses",
  "activa": true
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `politicaId = <id del response>`

---

### 2.2 Listar políticas
```
GET http://localhost:8002/api/politicas
```
**Respuesta esperada:** `200 OK`.

---

### 2.3 Obtener política por ID
```
GET http://localhost:8002/api/politicas/{{politicaId}}
```
**Respuesta esperada:** `200 OK`.

---

### 2.4 Actualizar política
```
PUT http://localhost:8002/api/politicas/{{politicaId}}
Content-Type: application/json

{
  "nombre": "Política Actualizada",
  "descripcion": "Cambio cada 5000 km o 6 meses",
  "activa": true
}
```
**Respuesta esperada:** `200 OK`.

---

### 2.5 Eliminar política
```
DELETE http://localhost:8002/api/politicas/{{politicaId}}
```
**Respuesta esperada:** `204 No Content`.

---

## 3. msvc-auditorias (puerto 8003)

Base URL: `http://localhost:8003/api/auditorias`

### 3.1 Registrar auditoria
```
POST http://localhost:8003/api/auditorias
Content-Type: application/json

{
  "entidad": "OrdenServicio",
  "entidadId": 1,
  "accion": "CREACION",
  "usuarioId": 1,
  "detalle": "Se creó la orden de servicio 1"
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `auditoriaId = <id del response>`

---

### 3.2 Listar auditorias
```
GET http://localhost:8003/api/auditorias
```
**Respuesta esperada:** `200 OK`.

---

### 3.3 Obtener auditoria por ID
```
GET http://localhost:8003/api/auditorias/{{auditoriaId}}
```
**Respuesta esperada:** `200 OK`.

---

## 4. msvc-clientes (puerto 8004)

Base URL: `http://localhost:8004/api/clientes`

### 4.1 Registrar cliente
```
POST http://localhost:8004/api/clientes
Content-Type: application/json

{
  "nombre": "Carlos Rodríguez",
  "dni": "12345678",
  "telefono": "999888777",
  "email": "carlos.rodriguez@gmail.com",
  "direccion": "Av. Principal 123"
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `clienteId = <id del response>`

---

### 4.2 Listar clientes
```
GET http://localhost:8004/api/clientes
```
**Respuesta esperada:** `200 OK`.

---

### 4.3 Buscar cliente por ID
```
GET http://localhost:8004/api/clientes/{{clienteId}}
```
**Respuesta esperada:** `200 OK`.

---

### 4.4 Actualizar cliente
```
PUT http://localhost:8004/api/clientes/{{clienteId}}
Content-Type: application/json

{
  "nombre": "Carlos Rodríguez Actualizado",
  "telefono": "998877665",
  "email": "carlos.actualizado@gmail.com"
}
```
**Respuesta esperada:** `200 OK`.

---

### 4.5 Eliminar cliente
```
DELETE http://localhost:8004/api/clientes/{{clienteId}}
```
**Respuesta esperada:** `204 No Content`.

---

## 5. msvc-proveedor (puerto 8005)

Base URL: `http://localhost:8005/api/proveedores`

### 5.1 Registrar proveedor
```
POST http://localhost:8005/api/proveedores
Content-Type: application/json

{
  "ruc": "20123456789",
  "razonSocial": "Lubricantes SAC",
  "contacto": "comercial@lubricantes.com",
  "activo": true
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `proveedorId = <id del response>`

---

### 5.2 Listar proveedores
```
GET http://localhost:8005/api/proveedores
```
**Respuesta esperada:** `200 OK`.

---

### 5.3 Buscar proveedor por ID
```
GET http://localhost:8005/api/proveedores/{{proveedorId}}
```
**Respuesta esperada:** `200 OK`.

---

### 5.4 Actualizar proveedor
```
PUT http://localhost:8005/api/proveedores/{{proveedorId}}
Content-Type: application/json

{
  "ruc": "20123456789",
  "razonSocial": "Lubricantes SAC Actualizado",
  "contacto": "ventas@lubricantes.com",
  "activo": true
}
```
**Respuesta esperada:** `200 OK`.

---

### 5.5 Eliminar proveedor
```
DELETE http://localhost:8005/api/proveedores/{{proveedorId}}
```
**Respuesta esperada:** `204 No Content`.
**Nota:** Fallará con `400` si el proveedor tiene recepciones registradas.

---

## 6. msvc-abastecimiento (puerto 8006)

Base URL: `http://localhost:8006/api/abastecimiento`
**Requiere:** msvc-proveedor (8005) e msvc-inventario (8007) levantados.

### 6.1 Listar recepciones
```
GET http://localhost:8006/api/abastecimiento
```
**Respuesta esperada:** `200 OK`.

---

### 6.2 Iniciar recepción de mercadería
```
POST http://localhost:8006/api/abastecimiento/iniciar?proveedorId={{proveedorId}}&documento=FAC-001-00001234
```
**Respuesta esperada:** `201 Created` (estado `PENDIENTE`).
**Guardar:** `recepcionId = <id del response>`

---

### 6.3 Buscar recepción por ID
```
GET http://localhost:8006/api/abastecimiento/{{recepcionId}}
```
**Respuesta esperada:** `200 OK`.

---

### 6.4 Agregar línea verificada
```
POST http://localhost:8006/api/abastecimiento/{{recepcionId}}/lineas?presentacion=1&cantidad=50&costo=25.50&lote=LOTE-2024-001&verificacion=CONFORME&productoId=1
```
**Valores de `verificacion`:** `CONFORME`, `NO_CONFORME`, `PENDIENTE`
**Respuesta esperada:** `200 OK`.
**Guardar:** `lineaId = <id de la línea>`

---

### 6.5 Registrar observación en línea
```
POST http://localhost:8006/api/abastecimiento/{{recepcionId}}/observaciones?lineaId={{lineaId}}&condicion=BUEN_ESTADO
```
**Valores de `condicion`:** `BUEN_ESTADO`, `DANADO`, `VENCIDO`
**Respuesta esperada:** `200 OK`.

---

### 6.6 Dar conformidad a la recepción
```
PUT http://localhost:8006/api/abastecimiento/{{recepcionId}}/conformar
```
**Respuesta esperada:** `200 OK` con estado `CONFORME`.

---

### 6.7 Cerrar recepción (sincroniza stock con msvc-inventario)
```
PUT http://localhost:8006/api/abastecimiento/{{recepcionId}}/cerrar
```
**Respuesta esperada:** `200 OK` con estado `CERRADO`.
**Efecto:** Llama a `POST /api/inventario/habilitar` en msvc-inventario (8007).

---

### 6.8 Verificar si proveedor tiene recepciones
```
GET http://localhost:8006/api/abastecimiento/existe-por-proveedor?proveedorId={{proveedorId}}
```
**Respuesta esperada:** `200 OK` con `true` o `false`.

---

## 7. msvc-inventario (puerto 8007)

Base URL: `http://localhost:8007/api/inventario`

### 7.1 Listar existencias
```
GET http://localhost:8007/api/inventario
```
**Respuesta esperada:** `200 OK`.

---

### 7.2 Habilitar stock
```
POST http://localhost:8007/api/inventario/habilitar?productoId=1&presentacionId=1&cantidad=50&recepcionId=1
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `existenciaId = <id del response>`

---

### 7.3 Buscar existencia por ID
```
GET http://localhost:8007/api/inventario/{{existenciaId}}
```
**Respuesta esperada:** `200 OK`.

---

### 7.4 Buscar por producto y presentación
```
GET http://localhost:8007/api/inventario/producto/1/presentacion/1
```
**Respuesta esperada:** `200 OK`.

---

### 7.5 Reservar stock
```
PUT http://localhost:8007/api/inventario/{{existenciaId}}/reservar?orden=101&cantidad=5
```
**Respuesta esperada:** `200 OK`.

---

### 7.6 Confirmar reserva
```
PUT http://localhost:8007/api/inventario/{{existenciaId}}/confirmar-reserva?orden=101
```
**Respuesta esperada:** `200 OK`.

---

### 7.7 Liberar reserva
```
PUT http://localhost:8007/api/inventario/{{existenciaId}}/liberar-reserva?orden=101
```
**Respuesta esperada:** `200 OK`.

---

### 7.8 Consumir stock
```
PUT http://localhost:8007/api/inventario/{{existenciaId}}/consumir?orden=101&cantidad=2
```
**Respuesta esperada:** `200 OK`.

---

### 7.9 Eliminar existencia
```
DELETE http://localhost:8007/api/inventario/{{existenciaId}}
```
**Respuesta esperada:** `204 No Content`.

---

## 8. msvc-vehiculos (puerto 8008)

Base URL: `http://localhost:8008/api/vehiculos`

### 8.1 Registrar vehículo
```
POST http://localhost:8008/api/vehiculos
Content-Type: application/json

{
  "placa": "ABC-123",
  "clienteId": {{clienteId}},
  "kilometrajeActual": 45000,
  "estado": "ACTIVO",
  "fichaTecnica": {
    "marca": "Toyota",
    "modelo": "Corolla",
    "anio": 2020,
    "motor": "1.8L",
    "cilindradaCc": 1798,
    "tipoCombustible": "GASOLINA"
  }
}
```
**Valores de `estado`:** `ACTIVO`, `INACTIVO`, `EN_MANTENIMIENTO`
**Valores de `tipoCombustible`:** `GASOLINA`, `DIESEL`, `GLP`, `GNV`, `HIBRIDO`, `ELECTRICO`
**Respuesta esperada:** `201 Created`.
**Guardar:** `vehiculoId = <id del response>`

---

### 8.2 Listar vehículos
```
GET http://localhost:8008/api/vehiculos
```
**Respuesta esperada:** `200 OK`.

---

### 8.3 Buscar vehículo por ID
```
GET http://localhost:8008/api/vehiculos/{{vehiculoId}}
```
**Respuesta esperada:** `200 OK`.

---

### 8.4 Buscar vehículo por placa
```
GET http://localhost:8008/api/vehiculos/placa/ABC-123
```
**Respuesta esperada:** `200 OK`.

---

### 8.5 Actualizar vehículo
```
PUT http://localhost:8008/api/vehiculos/{{vehiculoId}}
Content-Type: application/json

{
  "clienteId": {{clienteId}},
  "kilometrajeActual": 50000,
  "estado": "ACTIVO",
  "fichaTecnica": {
    "marca": "Toyota",
    "modelo": "Corolla",
    "anio": 2020,
    "motor": "1.8L",
    "cilindradaCc": 1798,
    "tipoCombustible": "GASOLINA"
  }
}
```
**Respuesta esperada:** `200 OK`.

---

### 8.6 Eliminar vehículo
```
DELETE http://localhost:8008/api/vehiculos/{{vehiculoId}}
```
**Respuesta esperada:** `204 No Content`.

---

## 9. msvc-historial-mantenimiento (puerto 8009)

Base URL: `http://localhost:8009/api/historiales`
**Requiere:** msvc-vehiculos (8008).

### 9.1 Registrar historial
```
POST http://localhost:8009/api/historiales
Content-Type: application/json

{
  "vehiculoId": {{vehiculoId}},
  "ordenId": {{ordenId}},
  "fechaServicio": "2025-06-15",
  "tipoServicio": "CAMBIO_ACEITE",
  "kilometraje": 45000,
  "descripcion": "Cambio de aceite 5W-30"
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `historialId = <id del response>`

---

### 9.2 Listar historiales
```
GET http://localhost:8009/api/historiales
```
**Respuesta esperada:** `200 OK`.

---

### 9.3 Buscar historial por ID
```
GET http://localhost:8009/api/historiales/{{historialId}}
```
**Respuesta esperada:** `200 OK`.

---

### 9.4 Historial por vehículo
```
GET http://localhost:8009/api/historiales/vehiculo/{{vehiculoId}}
```
**Respuesta esperada:** `200 OK`.

---

## 10. msvc-orden-servicio (puerto 8010)

Base URL: `http://localhost:8010/api/ordenes`
**Requiere:** msvc-clientes (8004), msvc-vehiculos (8008), msvc-historial-mantenimiento (8009).

### 10.1 Crear orden de servicio
```
POST http://localhost:8010/api/ordenes
Content-Type: application/json

{
  "clienteId": {{clienteId}},
  "vehiculoId": {{vehiculoId}},
  "descripcion": "Cambio de aceite y filtros",
  "kilometraje": 45000
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `ordenId = <id del response>`

---

### 10.2 Listar órdenes
```
GET http://localhost:8010/api/ordenes
```
**Respuesta esperada:** `200 OK`.

---

### 10.3 Buscar orden por ID
```
GET http://localhost:8010/api/ordenes/{{ordenId}}
```
**Respuesta esperada:** `200 OK`.

---

### 10.4 Aprobar orden
```
PUT http://localhost:8010/api/ordenes/{{ordenId}}/aprobar
```
**Respuesta esperada:** `200 OK` con estado `APROBADA`.

---

### 10.5 Cancelar orden
```
PUT http://localhost:8010/api/ordenes/{{ordenId}}/cancelar
```
**Respuesta esperada:** `200 OK` con estado `CANCELADA`.

---

## 11. msvc-ejecucion-mantenimiento (puerto 8011)

Base URL: `http://localhost:8011/api/ejecuciones`
**Requiere:** msvc-agenda-tecnico (8012), msvc-acopio-temporal (8013), msvc-orden-servicio (8010).

### 11.1 Crear ejecución
```
POST http://localhost:8011/api/ejecuciones
Content-Type: application/json

{
  "ordenId": {{ordenId}},
  "tecnicoUsuarioId": {{usuarioId}},
  "kilometraje": 45000
}
```
**Respuesta esperada:** `201 Created`.

---

### 11.2 Listar ejecuciones
```
GET http://localhost:8011/api/ejecuciones
```
**Respuesta esperada:** `200 OK`.

---

### 11.3 Obtener ejecución por ordenId
```
GET http://localhost:8011/api/ejecuciones/{{ordenId}}
```
**Respuesta esperada:** `200 OK`.

---

### 11.4 Iniciar ejecución
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/iniciar
```
**Respuesta esperada:** `200 OK`.

---

### 11.5 Suspender ejecución
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/suspender
```
**Respuesta esperada:** `200 OK`.

---

### 11.6 Registrar tarea
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/tareas
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

### 11.7 Iniciar tarea
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/tareas/{{tareaId}}/iniciar
```
**Respuesta esperada:** `200 OK`.

---

### 11.8 Completar tarea
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/tareas/{{tareaId}}/completar
```
**Respuesta esperada:** `200 OK`.

---

### 11.9 Registrar consumo de producto
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/consumos
Content-Type: application/json

{
  "productoId": 1,
  "cantidad": 4.5,
  "unidad": "LITROS"
}
```
**Respuesta esperada:** `200 OK`.

---

### 11.10 Declarar residuo generado
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/residuos
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

### 11.11 Calcular liquidación
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/liquidacion
```
**Respuesta esperada:** `200 OK` con el detalle de liquidación.

---

### 11.12 Cerrar ejecución
```
POST http://localhost:8011/api/ejecuciones/{{ordenId}}/cerrar
```
**Respuesta esperada:** `200 OK` con estado `CERRADO`.

---

## 12. msvc-agenda-tecnico (puerto 8012)

Base URL: `http://localhost:8012/api/agendas`

### 12.1 Obtener agenda de un técnico
```
GET http://localhost:8012/api/agendas/{{usuarioId}}
```
**Respuesta esperada:** `200 OK`.

---

### 12.2 Asignar trabajo al técnico
```
POST http://localhost:8012/api/agendas/{{usuarioId}}/asignaciones
Content-Type: application/json

{
  "ordenId": {{ordenId}},
  "inicio": "2025-06-15T08:00:00",
  "fin": "2025-06-15T12:00:00"
}
```
**Respuesta esperada:** `200 OK`.
**Guardar:** `asignacionId = <id de la asignación>`

---

### 12.3 Consultar disponibilidad
```
GET http://localhost:8012/api/agendas/{{usuarioId}}/disponibilidad?inicio=2025-06-15T08:00:00&fin=2025-06-15T12:00:00
```
**Respuesta esperada:** `200 OK` con `{ "disponible": true/false }`.

---

### 12.4 Iniciar asignación
```
POST http://localhost:8012/api/agendas/{{usuarioId}}/asignaciones/{{asignacionId}}/iniciar
```
**Respuesta esperada:** `200 OK` con estado `EN_CURSO`.

---

### 12.5 Liberar asignación
```
POST http://localhost:8012/api/agendas/{{usuarioId}}/asignaciones/{{asignacionId}}/liberar
```
**Respuesta esperada:** `200 OK` con estado `LIBERADA`.

---

### 12.6 Listar asignaciones del técnico
```
GET http://localhost:8012/api/agendas/{{usuarioId}}/asignaciones
```
**Respuesta esperada:** `200 OK`.

---

## 13. msvc-acopio-temporal (puerto 8013)

Base URL: `http://localhost:8013/api/acopios`

### 13.1 Crear acopio temporal
```
POST http://localhost:8013/api/acopios
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

### 13.2 Obtener acopio por tipo
```
GET http://localhost:8013/api/acopios/ACEITE_USADO
```
**Respuesta esperada:** `200 OK`.

---

### 13.3 Configurar capacidad
```
PUT http://localhost:8013/api/acopios/ACEITE_USADO/capacidad
Content-Type: application/json

{
  "capacidad": 1000.0,
  "unidad": "LITROS"
}
```
**Respuesta esperada:** `200 OK`.

---

### 13.4 Consultar estado del acopio
```
GET http://localhost:8013/api/acopios/ACEITE_USADO/estado
```
**Respuesta esperada:** `200 OK` con `cantidadAcopiada`, `capacidadDisponible` y `condicion`.

---

### 13.5 Registrar residuo generado
```
POST http://localhost:8013/api/acopios/ACEITE_USADO/residuos
Content-Type: application/json

{
  "ordenId": {{ordenId}},
  "declaracionOrigenId": 1,
  "cantidad": 3.5,
  "unidad": "LITROS",
  "fechaGeneracion": "2025-06-15T10:30:00"
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `residuoId = <id del response>`

---

### 13.6 Almacenar residuo
```
POST http://localhost:8013/api/acopios/ACEITE_USADO/residuos/{{residuoId}}/almacenar
```
**Respuesta esperada:** `200 OK`.

---

### 13.7 Listar residuos del acopio
```
GET http://localhost:8013/api/acopios/ACEITE_USADO/residuos
```
**Respuesta esperada:** `200 OK`.

---

## 14. msvc-entrega-operador (puerto 8014)

Base URL: `http://localhost:8014/api/entregas`
**Requiere:** msvc-operador-autorizado (8015) y msvc-acopio-temporal (8013).

### 14.1 Programar entrega a operador
```
POST http://localhost:8014/api/entregas
Content-Type: application/json

{
  "operadorId": {{operadorId}},
  "usuarioResponsableId": {{usuarioId}},
  "fechaEntrega": "2025-07-10"
}
```
**Respuesta esperada:** `201 Created`.
**Guardar:** `entregaId = <id del response>`

---

### 14.2 Listar entregas
```
GET http://localhost:8014/api/entregas
```
**Respuesta esperada:** `200 OK`.

---

### 14.3 Obtener entrega por ID
```
GET http://localhost:8014/api/entregas/{{entregaId}}
```
**Respuesta esperada:** `200 OK`.

---

### 14.4 Agregar línea de residuos
```
POST http://localhost:8014/api/entregas/{{entregaId}}/lineas
Content-Type: application/json

{
  "tipoResiduo": "ACEITE_USADO",
  "cantidad": 3.5,
  "unidad": "LITROS",
  "residuosIds": [{{residuoId}}]
}
```
**Respuesta esperada:** `200 OK`.
**Guardar:** `lineaEntregaId = <id de la línea>`

---

### 14.5 Eliminar línea de la entrega
```
DELETE http://localhost:8014/api/entregas/{{entregaId}}/lineas/{{lineaEntregaId}}
```
**Respuesta esperada:** `200 OK`.

---

### 14.6 Registrar manifiesto de residuos
```
POST http://localhost:8014/api/entregas/{{entregaId}}/manifiesto
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

### 14.7 Ejecutar entrega
```
POST http://localhost:8014/api/entregas/{{entregaId}}/ejecutar
```
**Respuesta esperada:** `200 OK`.

---

### 14.8 Conformar entrega
```
POST http://localhost:8014/api/entregas/{{entregaId}}/conformar
```
**Respuesta esperada:** `200 OK` con estado `CONFORME`.

---

## 15. msvc-operador-autorizado (puerto 8015)

Base URL: `http://localhost:8015/api/operadores`

### 15.1 Registrar operador autorizado
```
POST http://localhost:8015/api/operadores
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

### 15.2 Listar operadores
```
GET http://localhost:8015/api/operadores
```
**Respuesta esperada:** `200 OK`.

---

### 15.3 Obtener operador por ID
```
GET http://localhost:8015/api/operadores/{{operadorId}}
```
**Respuesta esperada:** `200 OK`.

---

### 15.4 Actualizar datos del operador
```
PUT http://localhost:8015/api/operadores/{{operadorId}}
Content-Type: application/json

{
  "razonSocial": "Residuos Industriales EIRL Actualizado",
  "telefono": "987654321"
}
```
**Respuesta esperada:** `200 OK`.

---

### 15.5 Actualizar autorización
```
PUT http://localhost:8015/api/operadores/{{operadorId}}/autorizacion
Content-Type: application/json

{
  "registroEors": "EORS-2025-0099",
  "desde": "2025-01-01",
  "hasta": "2026-12-31"
}
```
**Respuesta esperada:** `200 OK`.

---

### 15.6 Consultar vigencia del operador
```
GET http://localhost:8015/api/operadores/{{operadorId}}/vigencia?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK` con campo `vigente: true/false`.

---

### 15.7 Listar operadores vigentes
```
GET http://localhost:8015/api/operadores/vigentes?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK`.

---

## Flujo de integración completo recomendado

Ejecutar en este orden para demostrar la integración entre microservicios:

```
1.  [msvc-clientes]                POST /api/clientes                         → crea cliente
2.  [msvc-vehiculos]               POST /api/vehiculos                        → registra vehículo (clienteId)
3.  [msvc-proveedor]               POST /api/proveedores                      → crea proveedor
4.  [msvc-abastecimiento]          POST /api/abastecimiento/iniciar           → inicia recepción (proveedorId)
5.  [msvc-abastecimiento]          POST /api/abastecimiento/{id}/lineas       → agrega línea verificada
6.  [msvc-abastecimiento]          PUT  /api/abastecimiento/{id}/conformar
7.  [msvc-abastecimiento]          PUT  /api/abastecimiento/{id}/cerrar       → sincroniza inventario
8.  [msvc-inventario]              GET  /api/inventario                       → confirma stock habilitado
9.  [msvc-orden-servicio]          POST /api/ordenes                          → crea orden (clienteId, vehiculoId)
10. [msvc-agenda-tecnico]          POST /api/agendas/{usuarioId}/asignaciones → asigna técnico
11. [msvc-ejecucion-mantenimiento] POST /api/ejecuciones                     → crea ejecución (ordenId)
12. [msvc-ejecucion-mantenimiento] POST /api/ejecuciones/{id}/iniciar
13. [msvc-ejecucion-mantenimiento] POST /api/ejecuciones/{id}/tareas          → registra tarea
14. [msvc-acopio-temporal]         POST /api/acopios                          → crea acopio ACEITE_USADO
15. [msvc-acopio-temporal]         POST /api/acopios/ACEITE_USADO/residuos    → registra residuo
16. [msvc-acopio-temporal]         POST /api/acopios/ACEITE_USADO/residuos/{id}/almacenar
17. [msvc-operador-autorizado]     POST /api/operadores                       → registra operador
18. [msvc-entrega-operador]        POST /api/entregas                         → programa entrega (operadorId)
19. [msvc-entrega-operador]        POST /api/entregas/{id}/lineas             → agrega línea (residuoId)
20. [msvc-entrega-operador]        POST /api/entregas/{id}/manifiesto
21. [msvc-entrega-operador]        POST /api/entregas/{id}/ejecutar
22. [msvc-entrega-operador]        POST /api/entregas/{id}/conformar
```

---

## Configuración de base de datos

Todas las bases de datos se crean automáticamente al arrancar cada microservicio con `createDatabaseIfNotExist=true`.

- **Host:** `localhost:3306`
- **Usuario:** `root`
- **Contraseña:** `adminMILDER1234@`

---

*Lubrimax — Ingeniería de Software II*
