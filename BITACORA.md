# Bitácora de asistencia de IA

**Asignatura:** Lenguaje de Programación 3 (CYT646)
**Estudiante:** Mateo (GitHub: MatRF04)
**Dominio:** Minecraft

## Ejercicio POO-08 — CRUD REST, H2, reglas de juego y excepciones

### CRUD REST y H2

- **Asistente:** Codex (OpenAI)
- **Modelo:** GPT-6 Astra
- **Resumen de prompts:**
  - Pedí un CRUD REST para Entidad con arquitectura de tres capas, un service con
    validaciones y persistencia en H2 con Spring Data.
  - Pedí un comando curl para probar cada operación del CRUD.
  - Consulté la configuración de H2 en application.properties y el acceso a la consola.
- **Qué hizo la IA:** EntidadController, EntidadService, EntidadRepository, EntidadRequest,
  el mapeo JPA de Entidad y sus hijas, y la configuración de H2.

### Explosiones (2026-10-29)

- **Asistente:** OpenCode
- **Modelo:** GPT-6 Astra (OpenAI)
- **Pedido:** implementar una simulación REST con Creeper, Player y Zombie, conservando
  CRUD y H2, dos reglas, excepciones con handlers y colección de Insomnia.
- **Qué hizo la IA:** Creeper persistente, daño limitado por la vida, servicio transaccional,
  endpoint de explosión, jerarquía de errores, pruebas de integración y documentación.
- La simulación usa daño fijo de 10 hasta 3 bloques y distancias proporcionadas por el
  cliente; no implementa la fórmula exacta de Minecraft.

### Revisión contra el enunciado y la rúbrica (2026-11-09)

- **Asistente:** Claude Code (Anthropic)
- **Modelo:** Claude Opus 5.5 (`claude-opus-5-5`)

#### Resumen de prompts

1. Pedí revisar qué tenía y qué faltaba del proyecto según el enunciado de POO-08.
2. Pedí contrastar ese plan con la rúbrica antes de implementarlo.
3. Pedí implementarlo y dejar las correcciones de POO-06 para el final.

#### Qué hizo la IA

- Movió las reglas de la explosión de `ExplosionService` a `Creeper.explotar(List<ObjetivoExplosion>)`,
  para que el dominio lance cada hija de `ExplosionException`; el servicio solo carga y guarda.
- Agregó `@JsonTypeInfo`/`@JsonSubTypes` en `Entidad` para que el JSON lleve `tipo`.
- Agregó consultas propias al repositorio (`findByVidaGreaterThan`, `findCreepersListosParaExplotar`)
  con sus endpoints y pruebas.
- Movió `EntidadNoEncontradaException` a `domain.exception`.
- Completó la colección de Insomnia (GET /, POST/PUT ilegales, 404 de GET y DELETE,
  protocolo, GET posterior al DELETE) y la recorrió contra el servicio.
- Reescribió el README (propiedades H2, Insomnia, respuestas a "Qué hay que pensar",
  diagramas) y escribió el documento de entrega de POO-08.

### Qué revisé / decidí yo

- Elegí la regla de la simulación: la explosión del creeper, que muere al explotar y
  daña a las entidades cercanas.
- Los detalles de la implementación los propuso la IA y los acepté: el daño fijo de 10,
  el radio de 3 bloques, que solo afecte a Player y Zombie, que la distancia la informe
  el cliente, y que se validen todos los objetivos antes de aplicar daño.
- Decidí terminar POO-08 primero y dejar las correcciones de POO-06 para el final.
- Importé la colección en Insomnia, la recorrí completa (los 36 pedidos respondieron el
  código esperado) y la exporté desde la app.

### Corrección de POO-08 (2026-11-18)

- **Asistente:** Codex (OpenAI)
- **Modelo:** GPT-6
- **Resumen de prompts:** pedí aplicar la devolución docente de POO-08.
- **Qué hizo la IA:**
  - Pasó las validaciones y las dos reglas de la explosión de `Creeper` y `Entidad` a
    `EntidadService` y `ExplosionService`; el dominio ya no lanza excepciones.
  - Hizo que `EntidadService` rechace con 409 cualquier actualización de una entidad muerta
    o de un Creeper que ya explotó.
  - Agregó una prueba HTTP que verifica que la muerte es definitiva y los pedidos 30a–30d
    de Insomnia.
  - Actualizó el README y la respuesta a "¿Quién lanza la excepción?".

### Revisión de la corrección y ortografía (2026-11-22)

- **Asistente:** Claude Code (Anthropic)
- **Modelo:** Claude Opus 5.5 (`claude-opus-5-5`)
- **Resumen de prompts:**
  1. Pregunté si la branch de corrección de POO-08 estaba completa.
  2. Pasé la devolución del profesor y pedí compararla con la branch.
  3. Pedí eliminar los documentos de entregas anteriores y aplicar los arreglos pendientes.
- **Qué hizo la IA:** comparó la branch con cada criterio de la devolución, corrió las
  pruebas, eliminó los documentos de entrega de POO-06 y POO-08 y corrigió las tildes del
  README, de esta bitácora, del Javadoc y de los mensajes de error (con sus pruebas).
- **Qué decidí yo:** no volver a presentar la entrega de POO-08, eliminar los documentos de
  entregas anteriores y conservar `HELP.md`.

## Ejercicio POO-07 — Protocolo Sonoro / UICapable

- **Asistente:** OpenCode
- **Modelo:** GPT-6 Astra (OpenAI)
- **Resumen de prompts:** no conservé los prompts originales; de memoria, le pedí a
  OpenCode crear una interfaz para que el Cerdo y un bloque musical emitan sonido,
  un endpoint que los liste a través de la interfaz, y después ampliarla con imagen,
  posición y velocidad para la UI.
- **Qué decidí yo:** elegí el protocolo Sonoro con el Cerdo (Entidad) y el bloque
  musical (Bloque), dos familias distintas que emiten sonido.
- **Qué hizo la IA:** implementó las interfaces Sonoro y UICapable, las clases Bloque y
  BloqueMusical, los endpoints /sonidos y /ui y sus pruebas.

## Ejercicio POO-06 — Revisión, paquetes, constructores y sobrecarga

### Uso de IA

- **Asistente:** Claude Code (Anthropic)
- **Modelo:** Claude Sonnet 5.5 (`claude-sonnet-5-5`)

#### Resumen de prompts

1. Pedí revisar el enunciado del ejercicio y la rúbrica y analizar el proyecto para saber qué faltaba.
2. Pedí implementar: sobrecarga de un mensaje del dominio, manejo de errores de validación en
   el controller, actualización del README y creación de esta bitácora.
3. Pedí cómo construir las dos hijas desde la URL (punto 5) y que lo implementara, con
   tests y la documentación actualizada.
4. Pedí reescribir el documento de entrega para este ejercicio e incluir el hash del commit.

#### Qué hizo la IA

- Agregó `Esqueleto.disparar()` y `Esqueleto.disparar(int distancia)` (sobrecarga).
- Agregó `GET /entidades/esqueleto/disparar` y un `@ExceptionHandler` que responde 400 con el
  mensaje de la excepción de dominio.
- Cambió `ComportamientoController` para construir `Zombie` y `Aldeano` con parámetros de la
  URL (`GET /entidades/comportamientos?vida=..&danoBase=..&velocidad=..&hostilidad=..&comercializacion=..`),
  guardarlos en una `List<Entidad>` y devolver `describirComportamiento()` de cada uno sin
  condicionales por tipo.
- Movió el manejo del 400 a `ErrorController` (`@RestControllerAdvice`), para que cubra todos
  los endpoints.
- Agregó `ComportamientoControllerTests` (2 pruebas).
- Agregó `EntidadControllerTests` (3 pruebas) y ejecutó todas (6 en total) con `./mvnw test`.
- Reescribió el documento de entrega (objetivo, consignas, cómo probar, pregunta de anclaje).
- Actualizó el README (endpoints, apartado de sobrecarga/sobreescritura, diagrama).

#### Qué revisé / decidí yo

- Las reglas de validación siguen en `Entidad`; el controller no corrige valores.
- Los atributos siguen privados y sin setters, por lo que el controller no puede asignar
  vida o munición a mano.
