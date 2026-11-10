# Bitacora de asistencia de IA

**Asignatura:** Lenguaje de Programacion 3 (CYT646)
**Estudiante:** Mateo (GitHub: MatRF04)
**Dominio:** Minecraft

## Ejercicio POO-08 — CRUD REST, H2, reglas de juego y excepciones

### CRUD REST y H2

- **Asistente:** Codex (OpenAI)
- **Modelo:** GPT-6 Astra
- **Resumen de prompts:**
  - Pedi un CRUD REST para Entidad con arquitectura de tres capas, un service con
    validaciones y persistencia en H2 con Spring Data.
  - Pedi un comando curl para probar cada operacion del CRUD.
  - Consulte la configuracion de H2 en application.properties y el acceso a la consola.
- **Que hizo la IA:** EntidadController, EntidadService, EntidadRepository, EntidadRequest,
  el mapeo JPA de Entidad y sus hijas, y la configuracion de H2.

### Explosiones (2026-10-29)

- **Asistente:** OpenCode
- **Modelo:** GPT-6 Astra (OpenAI)
- **Pedido:** implementar una simulacion REST con Creeper, Player y Zombie, conservando
  CRUD y H2, dos reglas, excepciones con handlers y coleccion de Insomnia.
- **Que hizo la IA:** Creeper persistente, dano limitado por la vida, servicio transaccional,
  endpoint de explosion, jerarquia de errores, pruebas de integracion y documentacion.
- La simulacion usa dano fijo de 10 hasta 3 bloques y distancias proporcionadas por el
  cliente; no implementa la formula exacta de Minecraft.

### Revision contra el enunciado y la rubrica (2026-11-09)

- **Asistente:** Claude Code (Anthropic)
- **Modelo:** Claude Opus 5.5 (`claude-opus-5-5`)

#### Resumen de prompts

1. Pedi revisar que tenia y que faltaba del proyecto segun el enunciado de POO-08.
2. Pedi contrastar ese plan con la rubrica antes de implementarlo.
3. Pedi implementarlo y dejar las correcciones de POO-06 para el final.

#### Que hizo la IA

- Movio las reglas de la explosion de `ExplosionService` a `Creeper.explotar(List<ObjetivoExplosion>)`,
  para que el dominio lance cada hija de `ExplosionException`; el servicio solo carga y guarda.
- Agrego `@JsonTypeInfo`/`@JsonSubTypes` en `Entidad` para que el JSON lleve `tipo`.
- Agrego consultas propias al repositorio (`findByVidaGreaterThan`, `findCreepersListosParaExplotar`)
  con sus endpoints y pruebas.
- Movio `EntidadNoEncontradaException` a `domain.exception`.
- Completo la coleccion de Insomnia (GET /, POST/PUT ilegales, 404 de GET y DELETE,
  protocolo, GET posterior al DELETE) y la recorrio contra el servicio.
- Reescribio el README (propiedades H2, Insomnia, respuestas a "Que hay que pensar",
  diagramas) y escribio `ENTREGA_POO-08.md`.

### Que revise / decidi yo

- Elegi la regla de la simulacion: la explosion del creeper, que muere al explotar y
  dana a las entidades cercanas.
- Los detalles de la implementacion los propuso la IA y los acepte: el dano fijo de 10,
  el radio de 3 bloques, que solo afecte a Player y Zombie, que la distancia la informe
  el cliente, y que se validen todos los objetivos antes de aplicar dano.
- Decidi terminar POO-08 primero y dejar las correcciones de POO-06 para el final.
- Importe la coleccion en Insomnia, la recorri completa (los 36 pedidos respondieron el
  codigo esperado) y la exporte desde la app.

## Ejercicio POO-07 — Protocolo Sonoro / UICapable

- **Asistente:** OpenCode
- **Modelo:** GPT-6 Astra (OpenAI)
- **Resumen de prompts:** no conserve los prompts originales; de memoria, le pedi a
  OpenCode crear una interfaz para que el Cerdo y un bloque musical emitan sonido,
  un endpoint que los liste a traves de la interfaz, y despues ampliarla con imagen,
  posicion y velocidad para la UI.
- **Que decidi yo:** elegi el protocolo Sonoro con el Cerdo (Entidad) y el bloque
  musical (Bloque), dos familias distintas que emiten sonido.
- **Que hizo la IA:** implemento las interfaces Sonoro y UICapable, las clases Bloque y
  BloqueMusical, los endpoints /sonidos y /ui y sus pruebas.

## Ejercicio POO-06 — Revision, paquetes, constructores y sobrecarga

### Uso de IA

- **Asistente:** Claude Code (Anthropic)
- **Modelo:** Claude Sonnet 5.5 (`claude-sonnet-5-5`)

#### Resumen de prompts

1. Pedi revisar el enunciado del ejercicio y la rubrica y analizar el proyecto para saber que faltaba.
2. Pedi implementar: sobrecarga de un mensaje del dominio, manejo de errores de validacion en
   el controller, actualizacion del README y creacion de esta bitacora.
3. Pedi como construir las dos hijas desde la URL (punto 5) y que lo implementara, con
   tests y la documentacion actualizada.
4. Pedi reescribir `ENTREGA_CLASSROOM.md` para este ejercicio e incluir el hash del commit.

#### Que hizo la IA

- Agrego `Esqueleto.disparar()` y `Esqueleto.disparar(int distancia)` (sobrecarga).
- Agrego `GET /entidades/esqueleto/disparar` y un `@ExceptionHandler` que responde 400 con el
  mensaje de la excepcion de dominio.
- Cambio `ComportamientoController` para construir `Zombie` y `Aldeano` con parametros de la
  URL (`GET /entidades/comportamientos?vida=..&danoBase=..&velocidad=..&hostilidad=..&comercializacion=..`),
  guardarlos en una `List<Entidad>` y devolver `describirComportamiento()` de cada uno sin
  condicionales por tipo.
- Movio el manejo del 400 a `ErrorController` (`@RestControllerAdvice`), para que cubra todos
  los endpoints.
- Agrego `ComportamientoControllerTests` (2 pruebas).
- Agrego `EntidadControllerTests` (3 pruebas) y ejecuto todas (6 en total) con `./mvnw test`.
- Reescribio `ENTREGA_CLASSROOM.md` (objetivo, consignas, como probar, pregunta de anclaje).
- Actualizo el README (endpoints, apartado de sobrecarga/sobreescritura, diagrama).

#### Que revise / decidi yo

- Las reglas de validacion siguen en `Entidad`; el controller no corrige valores.
- Los atributos siguen privados y sin setters, por lo que el controller no puede asignar
  vida o municion a mano.
