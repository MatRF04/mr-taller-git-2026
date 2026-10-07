# Entrega — Ejercicio POO-06: Revisión, paquetes, constructores y sobrecarga

**Asignatura:** Lenguaje de Programación 3 (CYT646)  
**Edición:** 2026  
**Estudiante:** Mateo  
**Usuario de GitHub:** MatRF04  
**Dominio:** Minecraft  
**Repositorio:** [MatRF04/mr-taller-git-2026](https://github.com/MatRF04/mr-taller-git-2026)  
**Commit de la solución:** [62fd703e97e119d2faa4ac5b1e5cfd64e4a25c0c](https://github.com/MatRF04/mr-taller-git-2026/commit/62fd703e97e119d2faa4ac5b1e5cfd64e4a25c0c)

## Objetivo

Publicar un servicio HTTP con Spring Boot basado en el modelado de Minecraft, aplicando herencia, sobreescritura, ocultamiento de información, paquetes organizados, constructores simples y sobrecargados, y sobrecarga de un mensaje del dominio. Cualquier compañero debe poder clonar el repositorio, arrancar el servicio con `./mvnw spring-boot:run` y usarlo desde el navegador.

## Consignas aplicadas a mi dominio

1. **Paquetes:** el dominio está en `mr_taller_git_2026.domain` y los controllers REST en `mr_taller_git_2026.rest.controller`. `MrTallerGit2026Application` solo arranca la aplicación.
2. **Modelado:** `Entidad` (abstracta) y sus hijas `Player`, `Zombie`, `Cerdo`, `Aldeano`, `Esqueleto`; `ZombiePequeño` hereda de `Zombie`.
3. **Clase base abstracta:** `Entidad` declara `describirComportamiento()`; cada hija lo implementa a su manera.
4. **Controllers:**
   - `IndexController`: `GET /` confirma que el servicio está activo.
   - `EntidadController`: construye un `Zombie` (`/entidades/zombie`) y un `Esqueleto` (`/entidades/esqueleto/disparar`) con parámetros de la URL.
   - `ErrorController` (`@RestControllerAdvice`): cuando el dominio rechaza un valor (`IllegalArgumentException`), responde `400` con `{"error": "..."}`.
5. **JSON:** `ComportamientoController` construye un `Zombie` y un `Aldeano` desde la URL, los guarda en una `List<Entidad>` y devuelve el `describirComportamiento()` de cada uno, sin condicionales por tipo.
6. **Constructores:** cada hija tiene un constructor sin parámetros y otro con parámetros, ambos llaman a `super(...)` y dejan el objeto en un estado válido (`vida > 0`, `danoBase >= 0`, `velocidad >= 0`).
7. **Sobrecarga de mensajes:** `Esqueleto.disparar()` y `Esqueleto.disparar(int distancia)`.
8. **README:** licencia Apache 2.0, diagrama Mermaid y apartado "Sobrecarga y sobreescritura".
9. **BITACORA.md:** registra el uso de IA (Claude Code, Claude Sonnet 5.5) y el resumen de los prompts.

## Sobrecarga y sobreescritura

- **Sobreescritura:** `describirComportamiento()` se declara abstracto en `Entidad` y se reimplementa con `@Override` en `Player`, `Zombie`, `Cerdo`, `Aldeano` y `Esqueleto` (misma firma, distinta clase).
- **Sobrecarga:** `disparar()` y `disparar(int distancia)` en `Esqueleto` (mismo nombre, distinta lista de argumentos, misma clase).

## Cómo probarlo

Requisitos: Git y JDK 21.

```bash
git clone https://github.com/MatRF04/mr-taller-git-2026.git
cd mr-taller-git-2026
./mvnw test
./mvnw spring-boot:run
```

En Windows usar `mvnw.cmd`. Con el servicio iniciado:

| # | URL | Resultado esperado |
|---|---|---|
| 1 | `http://localhost:8080/` | `API de Minecraft de Mateo. Servicio funcionando.` |
| 2 | `http://localhost:8080/entidades/zombie?vida=20&danoBase=5&velocidad=3&hostilidad=true` | JSON con `vida`, `danoBase`, `velocidad` y `hostilidad` |
| 3 | `http://localhost:8080/entidades/comportamientos?vida=20&danoBase=5&velocidad=3&hostilidad=true&comercializacion=false` | Dos objetos: Zombie "Persigue y ataca al jugador." y Aldeano "No comercia con el jugador." |
| 4 | `http://localhost:8080/entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true` | `{"accion":"Dispara una flecha al jugador."}` |
| 5 | `http://localhost:8080/entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true&distancia=10` | `{"accion":"Dispara una flecha al jugador a 10 bloques."}` |
| 6 | `http://localhost:8080/entidades/zombie?vida=0&danoBase=5&velocidad=3&hostilidad=true` | `400` con `{"error":"La vida debe ser mayor que cero"}` |
| 7 | `http://localhost:8080/entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true&distancia=-4` | `400` con `{"error":"distancia no puede ser negativo"}` |

`./mvnw test` ejecuta 6 pruebas automatizadas (contexto, errores 400, sobrecarga y comportamientos).

## Pregunta de anclaje

**¿Qué ocurre si el controller asigna a mano la vida o la munición?**  
No puede hacerlo: los atributos de `Entidad` son privados y no existen setters públicos. El único camino para fijar un valor es el constructor, que lo valida y lanza `IllegalArgumentException` si viola una regla. El controller solo informa el resultado (400); no corrige ni modifica el estado. Así ninguna otra clase o capa puede dejar el objeto en un estado imposible.

## Entregables

- [x] Enlace exacto al commit de la solución (arriba).
- [x] README actualizado (licencia Apache 2.0, diagrama Mermaid, sobrecarga y sobreescritura).
- [x] `BITACORA.md` con el uso de IA.
- [x] Este archivo para Classroom.
- [ ] Usuario de GitHub (`MatRF04`) publicado en el chat del curso.
