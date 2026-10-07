# Bitacora — Ejercicio POO-06

**Asignatura:** Lenguaje de Programacion 3 (CYT646)
**Estudiante:** Mateo (GitHub: MatRF04)
**Dominio:** Minecraft

## Uso de IA

- **Asistente:** Claude Code (Anthropic)
- **Modelo:** Claude Sonnet 5.5 (`claude-sonnet-5-5`)

### Resumen de prompts

1. Pedi revisar el enunciado del ejercicio y la rubrica y analizar el proyecto para saber que faltaba.
2. Pedi implementar: sobrecarga de un mensaje del dominio, manejo de errores de validacion en
   el controller, actualizacion del README y creacion de esta bitacora.
3. Pedi como construir las dos hijas desde la URL (punto 5) y que lo implementara, con
   tests y la documentacion actualizada.

### Que hizo la IA

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
- Actualizo el README (endpoints, apartado de sobrecarga/sobreescritura, diagrama).

### Que revise / decidi yo

- Las reglas de validacion siguen en `Entidad`; el controller no corrige valores.
- Los atributos siguen privados y sin setters, por lo que el controller no puede asignar
  vida o municion a mano.
