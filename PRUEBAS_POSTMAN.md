# Guía de Pruebas Postman — Lubrimax Microservicios

> **Prerrequisitos**
> 1. MySQL 8 corriendo en `localhost:3306` con usuario `root` / contraseña `adminMILDER1234@`.
> 2. Los veintidós microservicios levantados en sus respectivos puertos.
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
|16 | msvc-producto                 | 8016   | msvc_lubrimax_producto                    |
|17 | msvc-servicio-mantenimiento  | 8017   | msvc_lubrimax_servicio_mantenimiento     |
|18 | msvc-matriz-compatibilidad   | 8018   | msvc_lubrimax_matriz_compatibilidad      |
|19 | msvc-lista-precio-base       | 8019   | msvc_lubrimax_lista_precio_base          |
|20 | msvc-comprobante             | 8020   | msvc_lubrimax_comprobante                 |
|21 | msvc-serie-comprobante       | 8021   | msvc_lubrimax_serie_comprobante           |
|22 | msvc-nota-credito            | 8022   | msvc_lubrimax_nota_credito                |

---

## 1. msvc-usuarios (puerto 8001)

Base URL: `http://localhost:8001/api/usuarios`

### 1.1 Crear usuario
```
POST http://localhost:8001/api/usuarios
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "documentoIdentidad": "12345678",
  "credencialAccesoEstado": "ACTIVO",
  "credencial": "secreto123",
  "rolId": 1
}
```
**Respuesta esperada:** `201 Created`.

---

### 1.2 Listar usuarios
```
GET http://localhost:8001/api/usuarios
```
**Respuesta esperada:** `200 OK`.

---

### 1.3 Buscar usuario por ID
```
GET http://localhost:8001/api/usuarios/1
```
**Respuesta esperada:** `200 OK`.

---

### 1.4 Asignar rol
```
PUT http://localhost:8001/api/usuarios/1/roles/1
```
**Respuesta esperada:** `200 OK`.

---

### 1.5 Cambiar estado
```
PUT http://localhost:8001/api/usuarios/1/estado
Content-Type: application/json

"INACTIVO"
```
**Respuesta esperada:** `200 OK`.

---

### 1.6 Eliminar usuario
```
DELETE http://localhost:8001/api/usuarios/1
```
**Respuesta esperada:** `200 OK` o `204 No Content`.

---

## 2. msvc-politicas (puerto 8002)

Base URL: `http://localhost:8002/api/politicas`

### 2.1 Crear política del negocio
```
POST http://localhost:8002/api/politicas
Content-Type: application/json

{
  "tipo": "ACEITE",
  "margen": 0.25,
  "periodoVigencia": "2026",
  "umbrales": {
    "stockMinimo": 10,
    "diasParaBajaRotacion": 30,
    "diasSinRotacion": 60,
    "limiteDeAcopio": 100.0
  }
}
```
**Respuesta esperada:** `201 Created`.

---

### 2.2 Obtener política por ID
```
GET http://localhost:8002/api/politicas/1
```
**Respuesta esperada:** `200 OK`.

---

### 2.3 Agregar promoción
```
PUT http://localhost:8002/api/politicas/1/promociones
Content-Type: application/json

{
  "descripcion": "Descuento del 10% por apertura",
  "activa": true
}
```
**Respuesta esperada:** `200 OK`.

---

## 3. msvc-auditorias (puerto 8003)

Base URL: `http://localhost:8003/api/auditorias`

### 3.1 Registrar auditoria
```
POST http://localhost:8003/api/auditorias
Content-Type: application/json

{
  "periodo": "2026-Q1",
  "usuarioIdResponsable": 1
}
```
**Respuesta esperada:** `201 Created`.

---

### 3.2 Consultar estado de auditoria
```
GET http://localhost:8003/api/auditorias/1
```
**Respuesta esperada:** `200 OK`.

---

### 3.3 Reportar hallazgo
```
POST http://localhost:8003/api/auditorias/1/hallazgos
Content-Type: application/json

{
  "tipo": "INCONSISTENCIA",
  "referencia": "ORDEN-1",
  "magnitud": 10.0
}
```
**Respuesta esperada:** `200 OK`.

---

### 3.4 Finalizar auditoría
```
PUT http://localhost:8003/api/auditorias/1/cierre
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
  "numeroDocumento": "12345678",
  "telefono": "999888777"
}
```
**Respuesta esperada:** `201 Created`.

---

### 4.2 Listar clientes
```
GET http://localhost:8004/api/clientes
```
**Respuesta esperada:** `200 OK`.

---

### 4.3 Buscar cliente por ID
```
GET http://localhost:8004/api/clientes/1
```
**Respuesta esperada:** `200 OK`.

---

### 4.4 Actualizar cliente
```
PUT http://localhost:8004/api/clientes/1
Content-Type: application/json

{
  "nombre": "Carlos Rodríguez Actualizado",
  "numeroDocumento": "12345678",
  "telefono": "998877665"
}
```
**Respuesta esperada:** `200 OK`.

---

### 4.5 Eliminar cliente
```
DELETE http://localhost:8004/api/clientes/1
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

---

### 5.2 Listar proveedores
```
GET http://localhost:8005/api/proveedores
```
**Respuesta esperada:** `200 OK`.

---

### 5.3 Buscar proveedor por ID
```
GET http://localhost:8005/api/proveedores/1
```
**Respuesta esperada:** `200 OK`.

---

### 5.4 Actualizar proveedor
```
PUT http://localhost:8005/api/proveedores/1
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
DELETE http://localhost:8005/api/proveedores/1
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
POST http://localhost:8006/api/abastecimiento/iniciar?proveedorId=1&documento=FAC-001-00001234
```
**Respuesta esperada:** `201 Created` (estado `RECIBIDO`).

---

### 6.3 Buscar recepción por ID
```
GET http://localhost:8006/api/abastecimiento/1
```
**Respuesta esperada:** `200 OK`.

---

### 6.4 Agregar línea verificada
```
POST http://localhost:8006/api/abastecimiento/1/lineas?presentacion=1&cantidad=50&costo=25.50&lote=LOTE-2024-001&verificacion=APROBADO&productoId=1
```
**Valores de `verificacion`:** `APROBADO`, `RECHAZADO`, `REQUIERE_REVISION`
**Respuesta esperada:** `200 OK`.

---

### 6.5 Registrar observación en línea
```
POST http://localhost:8006/api/abastecimiento/1/observaciones?lineaId=1&condicion=BUEN_ESTADO
```
**Valores de `condicion`:** `BUEN_ESTADO`, `DAÑADO`, `VENCIDO`, `FALTANTE`
**Respuesta esperada:** `200 OK`.

---

### 6.6 Dar conformidad a la recepción
```
PUT http://localhost:8006/api/abastecimiento/1/conformar
```
**Respuesta esperada:** `200 OK` con estado `CONFORME`.

---

### 6.7 Cerrar recepción (sincroniza stock con msvc-inventario)
```
PUT http://localhost:8006/api/abastecimiento/1/cerrar
```
**Respuesta esperada:** `200 OK` con estado `CERRADO`.
**Efecto:** Llama a `POST /api/inventario/habilitar` en msvc-inventario (8007).

---

### 6.8 Verificar si proveedor tiene recepciones
```
GET http://localhost:8006/api/abastecimiento/existe-por-proveedor?proveedorId=1
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

---

### 7.3 Buscar existencia por ID
```
GET http://localhost:8007/api/inventario/1
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
PUT http://localhost:8007/api/inventario/1/reservar?orden=101&cantidad=5
```
**Respuesta esperada:** `200 OK`.

---

### 7.6 Confirmar reserva
```
PUT http://localhost:8007/api/inventario/1/confirmar-reserva?orden=101
```
**Respuesta esperada:** `200 OK`.

---

### 7.7 Liberar reserva
```
PUT http://localhost:8007/api/inventario/1/liberar-reserva?orden=101
```
**Respuesta esperada:** `200 OK`.

---

### 7.8 Consumir stock
```
PUT http://localhost:8007/api/inventario/1/consumir?orden=101&cantidad=2
```
**Respuesta esperada:** `200 OK`.

---

### 7.9 Eliminar existencia
```
DELETE http://localhost:8007/api/inventario/1
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
  "clienteId": 1,
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
**Valores de `estado`:** `ACTIVO`, `INACTIVO`
**Valores de `tipoCombustible`:** `GASOLINA`, `DIESEL`, `GLP`, `GNV`, `HIBRIDO`, `ELECTRICO`
**Respuesta esperada:** `201 Created`.

---

### 8.2 Listar vehículos
```
GET http://localhost:8008/api/vehiculos
```
**Respuesta esperada:** `200 OK`.

---

### 8.3 Buscar vehículo por ID
```
GET http://localhost:8008/api/vehiculos/1
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
PUT http://localhost:8008/api/vehiculos/1
Content-Type: application/json

{
  "clienteId": 1,
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
DELETE http://localhost:8008/api/vehiculos/1
```
**Respuesta esperada:** `204 No Content`.

---

## 9. msvc-historial-mantenimiento (puerto 8009)

Base URL: `http://localhost:8009/api/historiales`
**Requiere:** msvc-vehiculos (8008).

### 9.1 Registrar mantenimiento en historial
```
POST http://localhost:8009/api/historiales/1/registros
Content-Type: application/json

{
  "ordenId": 1,
  "fechaAtencion": "2025-06-15",
  "kilometraje": 45000,
  "tecnicoId": 1,
  "costoTotal": 150.00,
  "servicioCerrado": true
}
```
**Respuesta esperada:** `200 OK`.

---

### 9.2 Listar historiales
```
GET http://localhost:8009/api/historiales
```
**Respuesta esperada:** `200 OK`.

---

### 9.3 Historial por vehículo
```
GET http://localhost:8009/api/historiales/1
```
**Respuesta esperada:** `200 OK`.

---

### 9.4 Resumen por vehículo
```
GET http://localhost:8009/api/historiales/1/resumen
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
  "clienteId": 1,
  "vehiculoId": 1,
  "kilometrajeCapturado": 45000,
  "mantenimientoSolicitado": {
    "tipoServicio": "PREVENTIVO",
    "descripcion": "Cambio de aceite y filtros"
  }
}
```
**Respuesta esperada:** `201 Created`.

---

### 10.2 Listar órdenes
```
GET http://localhost:8010/api/ordenes
```
**Respuesta esperada:** `200 OK`.

---

### 10.3 Buscar orden por ID
```
GET http://localhost:8010/api/ordenes/1
```
**Respuesta esperada:** `200 OK`.

---

### 10.4 Agregar propuesta
```
POST http://localhost:8010/api/ordenes/1/propuestas
Content-Type: application/json

{
  "descripcion": "Propuesta de cambio de aceite"
}
```
**Respuesta esperada:** `200 OK` / `201 Created`.

---

### 10.5 Autorizar orden
```
POST http://localhost:8010/api/ordenes/1/autorizaciones
Content-Type: application/json

{
  "medio": "PRESENCIAL",
  "alcance": "TOTAL",
  "responsableAutorizacion": "Carlos Rodríguez"
}
```
**Valores de `medio`:** `PRESENCIAL`, `LLAMADA`, `MENSAJERIA`
**Valores de `alcance`:** `TOTAL`, `PARCIAL`, `RECHAZO`
**Respuesta esperada:** `201 Created` con estado `AUTORIZADA`.

---

### 10.6 Emitir orden
```
PUT http://localhost:8010/api/ordenes/1/emitir
```
**Respuesta esperada:** `200 OK` con estado `EMITIDA`.

---

## 11. msvc-ejecucion-mantenimiento (puerto 8011)

Base URL: `http://localhost:8011/api/ejecuciones`
**Requiere:** msvc-agenda-tecnico (8012), msvc-acopio-temporal (8013), msvc-orden-servicio (8010).

### 11.1 Crear ejecución
```
POST http://localhost:8011/api/ejecuciones
Content-Type: application/json

{
  "ordenId": 1,
  "tecnicoUsuarioId": 1,
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
GET http://localhost:8011/api/ejecuciones/1
```
**Respuesta esperada:** `200 OK`.

---

### 11.4 Iniciar ejecución
```
POST http://localhost:8011/api/ejecuciones/1/iniciar
```
**Respuesta esperada:** `200 OK`.

---

### 11.5 Suspender ejecución
```
POST http://localhost:8011/api/ejecuciones/1/suspender
```
**Respuesta esperada:** `200 OK`.

---

### 11.6 Registrar tarea
```
POST http://localhost:8011/api/ejecuciones/1/tareas
Content-Type: application/json

{
  "servicioId": 1,
  "tipo": "DRENAJE_ACEITE"
}
```
**Valores de `tipo`:** `DRENAJE_ACEITE`, `CAMBIO_FILTRO`, `APLICACION_ADITIVO`, `REVISION_NIVELES`
**Respuesta esperada:** `200 OK`.

---

### 11.7 Iniciar tarea
```
POST http://localhost:8011/api/ejecuciones/1/tareas/1/iniciar
```
**Respuesta esperada:** `200 OK`.

---

### 11.8 Completar tarea
```
POST http://localhost:8011/api/ejecuciones/1/tareas/1/completar
```
**Respuesta esperada:** `200 OK`.

---

### 11.9 Registrar consumo de producto
```
POST http://localhost:8011/api/ejecuciones/1/consumos
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
POST http://localhost:8011/api/ejecuciones/1/residuos
Content-Type: application/json

{
  "tareaId": 1,
  "tipo": "ACEITE_USADO",
  "volumen": 3.5,
  "unidad": "LITROS"
}
```
**Valores de `tipo`:** `ACEITE_USADO`, `FILTRO_USADO`, `ENVASE_CONTAMINADO`, `MATERIAL_ABSORBENTE`
**Respuesta esperada:** `200 OK`.

---

### 11.11 Calcular liquidación
```
POST http://localhost:8011/api/ejecuciones/1/liquidacion
```
**Respuesta esperada:** `200 OK` con el detalle de liquidación.

---

### 11.12 Cerrar ejecución
```
POST http://localhost:8011/api/ejecuciones/1/cerrar
```
**Respuesta esperada:** `200 OK` con estado `CERRADO`.

---

## 12. msvc-agenda-tecnico (puerto 8012)

Base URL: `http://localhost:8012/api/agendas`

### 12.1 Obtener agenda de un técnico
```
GET http://localhost:8012/api/agendas/1
```
**Respuesta esperada:** `200 OK`.

---

### 12.2 Asignar trabajo al técnico
```
POST http://localhost:8012/api/agendas/1/asignaciones
Content-Type: application/json

{
  "ordenId": 1,
  "inicio": "2025-06-15T08:00:00",
  "fin": "2025-06-15T12:00:00"
}
```
**Respuesta esperada:** `200 OK`.

---

### 12.3 Consultar disponibilidad
```
GET http://localhost:8012/api/agendas/1/disponibilidad?inicio=2025-06-15T08:00:00&fin=2025-06-15T12:00:00
```
**Respuesta esperada:** `200 OK` con `{ "disponible": true/false }`.

---

### 12.4 Iniciar asignación
```
POST http://localhost:8012/api/agendas/1/asignaciones/1/iniciar
```
**Respuesta esperada:** `200 OK` con estado `EN_CURSO`.

---

### 12.5 Liberar asignación
```
POST http://localhost:8012/api/agendas/1/asignaciones/1/liberar
```
**Respuesta esperada:** `200 OK` con estado `LIBERADA`.

---

### 12.6 Listar asignaciones del técnico
```
GET http://localhost:8012/api/agendas/1/asignaciones
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
**Valores de `tipoResiduo`:** `ACEITE_USADO`, `FILTRO_USADO`, `ENVASE_CONTAMINADO`, `MATERIAL_ABSORBENTE`
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
  "ordenId": 1,
  "declaracionOrigenId": 1,
  "cantidad": 3.5,
  "unidad": "LITROS",
  "fechaGeneracion": "2025-06-15T10:30:00"
}
```
**Respuesta esperada:** `201 Created`.

---

### 13.6 Almacenar residuo
```
POST http://localhost:8013/api/acopios/ACEITE_USADO/residuos/1/almacenar
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
  "operadorId": 1,
  "usuarioResponsableId": 1,
  "fechaEntrega": "2025-07-10"
}
```
**Respuesta esperada:** `201 Created`.

---

### 14.2 Listar entregas
```
GET http://localhost:8014/api/entregas
```
**Respuesta esperada:** `200 OK`.

---

### 14.3 Obtener entrega por ID
```
GET http://localhost:8014/api/entregas/1
```
**Respuesta esperada:** `200 OK`.

---

### 14.4 Agregar línea de residuos
```
POST http://localhost:8014/api/entregas/1/lineas
Content-Type: application/json

{
  "tipoResiduo": "ACEITE_USADO",
  "cantidad": 3.5,
  "unidad": "LITROS",
  "residuosIds": [1]
}
```
**Respuesta esperada:** `200 OK`.

---

### 14.5 Eliminar línea de la entrega
```
DELETE http://localhost:8014/api/entregas/1/lineas/1
```
**Respuesta esperada:** `200 OK`.

---

### 14.6 Registrar manifiesto de residuos
```
POST http://localhost:8014/api/entregas/1/manifiesto
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
POST http://localhost:8014/api/entregas/1/ejecutar
```
**Respuesta esperada:** `200 OK`.

---

### 14.8 Conformar entrega
```
POST http://localhost:8014/api/entregas/1/conformar
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

---

### 15.2 Listar operadores
```
GET http://localhost:8015/api/operadores
```
**Respuesta esperada:** `200 OK`.

---

### 15.3 Obtener operador por ID
```
GET http://localhost:8015/api/operadores/1
```
**Respuesta esperada:** `200 OK`.

---

### 15.4 Actualizar datos del operador
```
PUT http://localhost:8015/api/operadores/1
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
PUT http://localhost:8015/api/operadores/1/autorizacion
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
GET http://localhost:8015/api/operadores/1/vigencia?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK` con campo `vigente: true/false`.

---

### 15.7 Listar operadores vigentes
```
GET http://localhost:8015/api/operadores/vigentes?fecha=2025-06-15
```
**Respuesta esperada:** `200 OK`.

---

## 16. msvc-producto (puerto 8016)

Directorio: `msvc-agregar-producto`. Base URL: `http://localhost:8016/api/productos`.

### Crear producto y presentación
```http
POST http://localhost:8016/api/productos
Content-Type: application/json

{
  "codigo": "ACE-5W30-001",
  "nombre": "Aceite sintético 5W-30",
  "marca": "LubriMax",
  "categoria": "ACEITE",
  "tipoAceite": "SINTETICO",
  "viscosidad": "5W-30",
  "especificacion": { "normaApi": "SP", "normaAcea": "C3" },
  "intervalo": { "kilometros": 10000, "meses": 12 },
  "estado": "ACTIVO",
  "presentaciones": [
    { "contenido": 1.0, "unidad": "LITRO", "envase": "BOTELLA", "activa": true }
  ]
}
```
Esperado: `201 Created`. 

```http
GET http://localhost:8016/api/productos
GET http://localhost:8016/api/productos/1
PUT http://localhost:8016/api/productos/1/desactivar
```
Esperado: `200 OK`.

```http
POST http://localhost:8016/api/productos/1/presentaciones
Content-Type: application/json

{ "contenido": 4.0, "unidad": "LITRO", "envase": "GALON", "activa": true }
```

---

## 17. msvc-servicio-mantenimiento (puerto 8017)

Base URL: `http://localhost:8017/api/servicios-mantenimiento`.

```http
POST http://localhost:8017/api/servicios-mantenimiento
Content-Type: application/json

{
  "tipo": "CAMBIO_ACEITE",
  "descripcion": "Cambio preventivo de aceite de motor",
  "activo": true,
  "categoriasConsumidas": ["ACEITE"]
}
```
Esperado: `201 Created`. 

```http
GET http://localhost:8017/api/servicios-mantenimiento
GET http://localhost:8017/api/servicios-mantenimiento/1
DELETE http://localhost:8017/api/servicios-mantenimiento/1
```
Esperado: `200 OK`, `200 OK` y `204 No Content`.

---

## 18. msvc-matriz-compatibilidad (puerto 8018)

Base URL: `http://localhost:8018/api/matrices-compatibilidad`.

```http
POST http://localhost:8018/api/matrices-compatibilidad
Content-Type: application/json

{
  "reglas": [{
    "criterio": {
      "tipoDeMotor": "GASOLINA",
      "cilindrada": 1800,
      "combustible": "GASOLINA",
      "anioDesde": 2015,
      "anioHasta": 2026
    },
    "productosAdmitidos": [1]
  }]
}
```
Esperado: `201 Created`. 

```http
GET http://localhost:8018/api/matrices-compatibilidad/1/compatible?motor=GASOLINA&cilindrada=1800&combustible=GASOLINA&anio=2020&productoId=1
GET http://localhost:8018/api/matrices-compatibilidad/1/productos?motor=GASOLINA&cilindrada=1800&combustible=GASOLINA&anio=2020
```
Esperado: `200 OK`, `compatible: true` y una lista con `productoId`.

---

## 19. msvc-lista-precio-base (puerto 8019)

Base URL: `http://localhost:8019/api/listas-precios`.

```http
POST http://localhost:8019/api/listas-precios
Content-Type: application/json

{
  "vigencia": { "desde": "2026-01-01T00:00:00", "hasta": "2026-12-31T23:59:59" },
  "precios": []
}
```
Esperado: `201 Created`. 

```http
POST http://localhost:8019/api/listas-precios/1/precios
Content-Type: application/json

{ "tipoReferencia": "PRESENTACION", "referenciaId": 1, "precio": 100.00, "moneda": "PEN" }
```

```http
PUT http://localhost:8019/api/listas-precios/1/activar
GET http://localhost:8019/api/listas-precios/vigente/precio?tipo=PRESENTACION&referenciaId=1
```
Esperado: `200 OK`.

---

## 20. msvc-serie-comprobante (puerto 8021)

Base URL: `http://localhost:8021/api/series`. Levantar antes de comprobantes y notas.

```http
POST http://localhost:8021/api/series
Content-Type: application/json

{ "codigo": "B001", "tipo": "BOLETA", "rangoInicio": 1, "rangoFin": 99999999, "activa": true }
```
Esperado: `201 Created`. 

```http
POST http://localhost:8021/api/series
Content-Type: application/json

{ "codigo": "BC01", "tipo": "NOTA_CREDITO", "rangoInicio": 1, "rangoFin": 99999999, "activa": true }
```

```http
POST http://localhost:8021/api/series/1/siguiente
```
Esperado: `200 OK` y `{"numero":"B001-00000001"}`. El comprobante siguiente usará el correlativo 2.

---

## 21. msvc-comprobante (puerto 8020)

Base URL: `http://localhost:8020/api/comprobantes`. Requiere ejecución cerrada en 8011 y serie en 8021. Las líneas más 18% de IGV deben coincidir con `liquidacion.total`.

```http
POST http://localhost:8020/api/comprobantes
Content-Type: application/json

{
  "ordenId": 1,
  "clienteId": 1,
  "serieId": 1,
  "tipo": "BOLETA",
  "receptorNombre": "Carlos Rodríguez",
  "receptorDocumento": "12345678",
  "moneda": "PEN",
  "lineas": [{
    "tipo": "MANO_DE_OBRA",
    "referenciaId": 1,
    "descripcion": "Cambio preventivo de aceite",
    "cantidad": 1,
    "precioUnitario": 100.00
  }]
}
```
Esperado: `201 Created`. 

```http
POST http://localhost:8020/api/comprobantes/1/pagos
Content-Type: application/json

{ "medio": "YAPE", "monto": 118.00 }
```
Esperado: `200 OK` y estado `PAGADO`.

```http
GET http://localhost:8020/api/comprobantes
GET http://localhost:8020/api/comprobantes/1
```

---

## 22. msvc-nota-credito (puerto 8022)

Base URL: `http://localhost:8022/api/notas-credito`. Requiere comprobante en 8020 y serie de nota en 8021.

```http
POST http://localhost:8022/api/notas-credito
Content-Type: application/json

{
  "comprobanteId": 1,
  "serieId": 2,
  "motivo": "ERROR_MONTO",
  "monto": 18.00,
  "moneda": "PEN"
}
```
Esperado: `201 Created`. 

```http
GET http://localhost:8022/api/notas-credito
GET http://localhost:8022/api/notas-credito/1
```
Esperado: `200 OK`.

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
23. [msvc-producto]                POST /api/productos                         → registra producto y presentación
24. [msvc-servicio-mantenimiento]  POST /api/servicios-mantenimiento           → registra servicio preventivo
25. [msvc-matriz-compatibilidad]   POST /api/matrices-compatibilidad           → vincula ficha técnica y producto
26. [msvc-lista-precio-base]       POST /api/listas-precios                    → crea lista
27. [msvc-lista-precio-base]       POST /api/listas-precios/{id}/precios       → agrega precios
28. [msvc-lista-precio-base]       PUT  /api/listas-precios/{id}/activar       → deja una única lista vigente
29. [msvc-serie-comprobante]       POST /api/series                            → crea series fiscales
30. [msvc-comprobante]             POST /api/comprobantes                      → revalida ejecución cerrada y emite
31. [msvc-comprobante]             POST /api/comprobantes/{id}/pagos           → completa el pago
32. [msvc-nota-credito]            POST /api/notas-credito                     → ajusta el comprobante
```

---

## Configuración de base de datos

Todas las bases de datos se crean automáticamente al arrancar cada microservicio con `createDatabaseIfNotExist=true`.

- **Host:** `localhost:3306`
- **Usuario:** `root`
- **Contraseña:** `adminMILDER1234@`

---

*Lubrimax — Ingeniería de Software II*
