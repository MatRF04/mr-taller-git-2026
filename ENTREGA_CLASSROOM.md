# Bitácora — Taller de Git y modelado orientado a objetos

**Asignatura:** Lenguaje de Programación 3 (CYT646)  
**Edición:** 2026  
**Estudiante:** Mateo  
**Usuario de GitHub:** MatRF04  
**Dominio:** Minecraft  
**Repositorio:** [MatRF04/mr-taller-git-2026](https://github.com/MatRF04/mr-taller-git-2026)  
**Commit de la solución:** [62fd703e97e119d2faa4ac5b1e5cfd64e4a25c0c](https://github.com/MatRF04/mr-taller-git-2026/commit/62fd703e97e119d2faa4ac5b1e5cfd64e4a25c0c)

## Objetivo

Versionar un modelo orientado a objetos con Git, trabajar mediante ramas y pull requests y exponer el modelo mediante una API HTTP con Spring Boot. Aplicar herencia, encapsulamiento, validación y polimorfismo, manteniendo las reglas del dominio dentro de las entidades.

## Consignas del ejercicio

- Utilizar un repositorio público con README y licencia Apache 2.0.
- Incorporar un proyecto Spring Boot con Maven, Java 21 y soporte web.
- Versionar las clases del modelo y practicar el ciclo de trabajo con Git.
- Participar en una colaboración mediante rama, pull request, revisión y merge.
- Implementar un endpoint de presentación y otro de construcción mediante parámetros de URL.
- Declarar un método abstracto en la clase base e implementarlo en al menos dos hijas independientes.
- Devolver JSON con los comportamientos utilizando el tipo padre, sin condicionales por tipo concreto.
- Documentar el modelo mediante un diagrama Mermaid en el README.
- Adjuntar en Classroom un archivo con las especificaciones y la forma de probar el proyecto.

## Registro de trabajo

Las entradas se presentan en orden de realización, sin asignar fechas no registradas.

### Entrada 1 — Preparación del proyecto y versionado

Se incorporó el proyecto Spring Boot al repositorio `mr-taller-git-2026`. Se agregaron las clases del dominio Minecraft y se utilizaron commits para registrar los avances.

El modelo parte de `Entidad`, que reúne los atributos comunes `vida`, `danoBase` y `velocidad`. Sus especializaciones son `Player`, `Zombie`, `Cerdo`, `Aldeano` y `Esqueleto`. La clase `ZombiePequeño` hereda de `Zombie`.

### Entrada 2 — Colaboración mediante GitHub

Se recibió una contribución en la rama `eugeejercicio17/09` y se integró a `main` mediante el pull request número 1. El historial online registra el merge el 17 de septiembre de 2026, con el commit `9c270b8`.

**Evidencia:** [Pull request #1](https://github.com/MatRF04/mr-taller-git-2026/pull/1).

Esta entrada documenta la contribución recibida en mi repositorio; el PR propio en el repositorio de un compañero requiere su evidencia por separado.

### Entrada 3 — Endpoint de presentación

Se modificó `IndexController` para responder a `GET /` con el siguiente mensaje:

```text
API de Minecraft de Mateo. Servicio funcionando.
```

El endpoint identifica al autor y al dominio, y permite comprobar que la aplicación responde por HTTP.

### Entrada 4 — Construcción de una entidad desde la URL

Se creó `EntidadController` con la ruta `GET /entidades/zombie`. Recibe `vida`, `danoBase`, `velocidad` y `hostilidad` mediante `@RequestParam` y los pasa al constructor de `Zombie`.

El controller devuelve la entidad como JSON. Las reglas de validez pertenecen al modelo; el controller no corrige los valores recibidos ni modifica atributos directamente.

### Entrada 5 — Método abstracto y polimorfismo

Se agregó a `Entidad` el método abstracto:

```java
public abstract String describirComportamiento();
```

Se implementó en las clases concretas:

| Entidad | Comportamiento |
|---|---|
| `Zombie` | Persigue y ataca al jugador si es hostil; de lo contrario, deambula sin atacar. |
| `Aldeano` | Informa si comercia con el jugador. |
| `Cerdo` | Informa si puede ser montado. |
| `Esqueleto` | Ataca con arco o cuerpo a cuerpo según su configuración. |
| `Player` | Explora el mundo y combate a las entidades. |
| `ZombiePequeño` | Hereda el comportamiento de `Zombie` y agrega el atributo `velocidadAumentada`. |

Se creó `ComportamientoController`, que construye un `Zombie` y un `Aldeano`, los almacena en una `List<Entidad>` y llama a `describirComportamiento()` sobre cada elemento. La selección del comportamiento se realiza por polimorfismo, sin un `if` por tipo en el controller.

### Entrada 6 — Encapsulamiento y validación

Se eliminaron los setters públicos de las entidades y se conservaron los atributos privados. Los getters permiten consultar el estado y serializarlo como JSON.

Se incorporaron las siguientes reglas en los constructores:

- `vida` debe ser mayor que cero.
- `danoBase` y `velocidad` no pueden ser negativos.
- `velocidadAumentada`, en `ZombiePequeño`, no puede ser negativa.
- El constructor sin parámetros de `Entidad` establece valores válidos: vida 1, daño 0 y velocidad 0.

Los valores inválidos provocan una `IllegalArgumentException`, que `ErrorController` (`@RestControllerAdvice`) traduce a una respuesta HTTP 400 con el mensaje del dominio.

El estado queda protegido porque no se puede asignar libremente desde los controllers. El padre declara el contrato común, pero cada hija define su comportamiento particular. Así, la entrada HTTP queda separada de las reglas del modelo.

### Entrada 7 — Documentación del modelo

Se incorporó al README el diagrama de clases basado en el archivo original del modelado. Se incluyeron `Esqueleto`, el método abstracto, las relaciones de herencia y la visibilidad privada de los atributos, junto con una descripción de los endpoints y comportamientos.

**Documentación:** [README del proyecto](https://github.com/MatRF04/mr-taller-git-2026#readme).

### Entrada 8 — Verificación y preparación de la entrega

Durante los cambios de controllers, polimorfismo y encapsulamiento se ejecutó `./mvnw -q compile` con resultado satisfactorio. Al introducir el método abstracto, la compilación detectó que `Esqueleto` también debía implementarlo; se agregó esa implementación y se volvió a compilar correctamente.

Esta bitácora incluye a continuación el procedimiento de prueba HTTP y los resultados esperados. Esos ejemplos son criterios de comprobación, no un registro de pruebas HTTP automatizadas ejecutadas.

## Cómo ejecutar

Requisitos: Git, JDK 21 y conexión a Internet para descargar las dependencias mediante Maven Wrapper.

```bash
git clone https://github.com/MatRF04/mr-taller-git-2026.git
cd mr-taller-git-2026
./mvnw -q compile
./mvnw spring-boot:run
```

En Windows se puede utilizar `mvnw.cmd` en lugar de `./mvnw`. Con la aplicación iniciada, abrir `http://localhost:8080/`. Para detenerla, presionar `Ctrl+C` en la terminal.

## Cómo probar y resultados esperados

### 1. Presentación del servicio

Abrir en el navegador o realizar un GET desde Insomnia:

```text
http://localhost:8080/
```

**Resultado esperado:** el mensaje de presentación de Mateo y del dominio Minecraft.

### 2. Construcción válida

```text
http://localhost:8080/entidades/zombie?vida=20&danoBase=5&velocidad=3&hostilidad=true
```

**Resultado esperado:** JSON con los valores de la entidad, independientemente del orden de sus propiedades:

```json
{
  "vida": 20,
  "danoBase": 5,
  "velocidad": 3,
  "hostilidad": true
}
```

### 3. Comportamientos polimórficos

```text
http://localhost:8080/entidades/comportamientos?vida=20&danoBase=5&velocidad=3&hostilidad=true&comercializacion=true
```

**Resultado esperado:**

```json
[
  {
    "tipo": "Zombie",
    "vida": 20,
    "comportamiento": "Persigue y ataca al jugador."
  },
  {
    "tipo": "Aldeano",
    "vida": 20,
    "comportamiento": "Comercia con el jugador."
  }
]
```

### 4. Rechazo de valores inválidos

```text
http://localhost:8080/entidades/zombie?vida=-1&danoBase=5&velocidad=3&hostilidad=true
```

**Resultado esperado:** respuesta `400 Bad Request` con `{"error": "La vida debe ser mayor que cero"}`. Repetir con `vida=0`, daño negativo o velocidad negativa.

## Criterios de aceptación y cierre

Antes de entregar, comprobar lo siguiente:

- El repositorio es público y contiene README y licencia Apache 2.0.
- La configuración de Spring Boot y Java cumple la versión solicitada por la cátedra.
- El proyecto compila y arranca desde Maven Wrapper.
- Los tres endpoints responden según los ejemplos anteriores.
- Los constructores rechazan valores inválidos y no hay setters públicos que permitan romper las reglas.
- La clase base tiene un método abstracto y al menos dos hijas independientes lo implementan.
- El controller solicita el comportamiento mediante el tipo `Entidad`.
- El diagrama Mermaid se visualiza en GitHub y coincide con las clases reales, incluido el nombre `ZombiePequeño`.
- Los cambios finales están publicados en `main`.
- Se dispone de la evidencia del PR propio al repositorio del compañero, además del PR recibido.
- Se realizaron la revisión de historial y el checkout de un commit con vuelta a `main`, y se compartió el usuario de GitHub en el chat del curso.
- La configuración SSH se realiza y verifica en la máquina host, según la consigna.
- Se adjunta este archivo Markdown a la tarea de Classroom junto con el enlace al repositorio.

## Conclusión

El trabajo permitió pasar de clases de un modelo de Minecraft a una API HTTP que construye entidades y expone sus comportamientos. La herencia reúne el estado común, los constructores protegen las reglas del dominio y el polimorfismo permite pedir un mismo mensaje a entidades diferentes. Git y GitHub registran la evolución del proyecto y la integración de una contribución mediante un pull request.
