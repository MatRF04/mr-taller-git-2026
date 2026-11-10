# Entrega — Ejercicio POO-08: CRUD REST, H2, reglas de juego y excepciones

**Asignatura:** Lenguaje de Programación 3 (CYT646)  
**Edición:** 2026  
**Estudiante:** Mateo  
**Usuario de GitHub:** MatRF04  
**Dominio:** Minecraft  
**Repositorio:** [MatRF04/mr-taller-git-2026](https://github.com/MatRF04/mr-taller-git-2026)  
**Commit de la solución:** COMMIT_PENDIENTE

## Objetivo

Publicar un servicio HTTP con Spring Boot sobre el modelado de Minecraft que guarda las
entidades en H2, expone un CRUD REST de la familia `Entidad`, simula explosiones de creepers
con dos reglas del juego que se persisten, distingue cada error con una jerarquía de
excepciones y un handler por tipo, y se prueba con una colección de Insomnia versionada.

## Consignas aplicadas a mi dominio

1. **Dependencias y H2:** `spring-boot-starter-data-jpa`, driver H2 y consola en `/h2-console`.
   La base vive en `./data/entidades` (`ddl-auto=update`, usuario `sa`).
2. **Tres clases persistidas:** `Creeper`, `Player` y `Zombie`, hijas de la clase abstracta
   `Entidad` (`@Inheritance(SINGLE_TABLE)`). `describirComportamiento()` sigue abstracto y
   cada hija lo sobreescribe. El constructor vacío de `Entidad` es `protected` para JPA.
3. **Protocolo (POO-07):** `Sonoro`/`UICapable`, implementado por `Cerdo` (`Entidad`) y
   `BloqueMusical` (`Bloque`); lo usan `GET /sonidos` y `GET /ui`.
4. **Repositorio:** `EntidadRepository extends JpaRepository<Entidad, Long>` con dos consultas
   propias: `findByVidaGreaterThan(int)` y `findCreepersListosParaExplotar()`.
5. **CRUD:** `POST /entidades` (201), `GET /entidades`, `GET /entidades/{id}` (200/404),
   `PUT /entidades/{id}` (200), `DELETE /entidades/{id}` (204). El JSON lleva `tipo` para
   distinguir el subtipo (`@JsonTypeInfo`).
6. **Dos reglas** (métodos de `Creeper`):
   1. Un creeper vivo explota una sola vez y muere al hacerlo.
   2. La explosión causa 10 de daño a cada `Player` o `Zombie` a 3 bloques o menos, sin bajar su vida de cero.
7. **Simulación:** `POST /entidades/{id}/explotar` con `{"objetivos":[{"id":..,"distancia":..}]}`.
   El resultado se persiste y un `GET` posterior lo muestra.
8. **Excepciones:** `ExplosionException` (base, no comprobada) con las hijas
   `TipoEntidadInvalidoException`, `DistanciaInvalidaException`, `ObjetivoDuplicadoException` (400),
   `EntidadMuertaException` y `CreeperYaExplotoException` (409); aparte,
   `EntidadNoEncontradaException` (404). `ErrorController` tiene un handler por tipo.

## Invariantes

- `vida > 0` al construir o actualizar; `danoBase`, `velocidad`, `velocidadAumentada` y
  `distancia` no negativos. Después de recibir daño la vida nunca baja de 0.
- No hay setters de `vida`, `danoBase`, `velocidad`, `id` ni `yaExploto`.
- Un creeper que ya explotó no puede volver a explotar ni el CRUD puede reiniciarlo.
- Una explosión inválida no deja daños parciales: el creeper valida todos los objetivos antes de cambiar estado.
- La entrada JSON nunca se deserializa directo a `Entidad`: pasa por los constructores de negocio.

## Cómo probarlo

Requisitos: Git y JDK 21.

```bash
git clone https://github.com/MatRF04/mr-taller-git-2026.git
cd mr-taller-git-2026
./mvnw test
./mvnw spring-boot:run
```

1. En Insomnia: **Import → From File → `insomnia/Minecraft.yaml`**.
2. Ejecutar los pedidos en orden numérico. En `01`–`04` copiar el `id` de cada respuesta a
   `creeper_id`, `player_id`, `zombie_id` y `otro_creeper_id` del entorno.

| Pedidos | Qué se ve |
| --- | --- |
| `00` | `GET /` responde que el servicio está vivo |
| `01`–`08` | Crear (201 con id), listar, leer por id, actualizar y releer el cambio |
| `09`–`11` | POST `vida:0` y PUT `danoBase:-1` → 400; el listado no creció |
| `12`–`13` | GET y DELETE de un id inexistente → 404 |
| `14`–`16` | Protocolo `/sonidos`, `/ui` y consulta `/entidades/creepers/listos` |
| `17`–`25` | Un error por cada hija de la jerarquía; la base no cambia |
| `26`–`30` | Simulación feliz: Player queda en 10, Zombie en 0, Creeper en 0 con `yaExploto: true` |
| `31`–`33` | Creeper ya explotó (409), objetivo muerto (409), fuera del radio (200 sin daño) |
| `34`–`35` | DELETE (204) y GET posterior (404) |

Consola H2: `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:file:./data/entidades`,
usuario `sa`, sin clave → `SELECT * FROM ENTIDAD;`

## Preguntas de anclaje

**Si mañana cambiamos H2 por otra base, ¿qué archivos se tocan?** Solo `pom.xml` (driver) y
`application.properties` (URL y credenciales). Dominio, repositorio, servicios y controllers quedan igual.

**¿Quién lanza la excepción, el controller o el objeto del juego?** El objeto del juego:
`Creeper` lanza las hijas de `ExplosionException` y `Entidad` rechaza valores inválidos.
El controller solo dispara la regla y `ErrorController` traduce cada tipo a un código HTTP.

**¿Puede otra capa dejar el objeto en un estado imposible?** No: ni el controller, ni el JSON,
ni JPA pueden asignar vida o el estado de explosión sin pasar por los mensajes del dominio.
