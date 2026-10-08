# mr-taller-git-2026

API REST de entidades de Minecraft desarrollada para Lenguaje de Programacion 3.

## Como ejecutar

```bash
./mvnw spring-boot:run
```

La aplicacion queda disponible en `http://localhost:8080`.

## Endpoints

### Estado del servicio

```text
GET /
```

Devuelve un mensaje indicando el autor, el dominio y que el servicio esta funcionando.

### Construccion de un zombie

```text
GET /entidades/zombie?vida=20&danoBase=5&velocidad=3&hostilidad=true
```

Construye un `Zombie` usando los parametros recibidos por URL y devuelve sus datos en JSON.
Los valores se validan en las entidades del dominio.

### Disparo del esqueleto (sobrecarga)

```text
GET /entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true
GET /entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true&distancia=10
```

Sin `distancia` se invoca `disparar()`; con `distancia` se invoca `disparar(int)`.

### Errores de validacion

Si un valor viola una regla del dominio (por ejemplo `vida=0`), la entidad lanza
`IllegalArgumentException` y el controller responde `400 Bad Request` con
`{"error": "<mensaje>"}`.

### Comportamientos

```text
GET /entidades/comportamientos?vida=20&danoBase=5&velocidad=3&hostilidad=true&comercializacion=false
```

Construye un `Zombie` y un `Aldeano` con los parametros de la URL y devuelve en JSON el
comportamiento de cada uno. El controller los guarda en una `List<Entidad>` y llama a
`describirComportamiento()` sin distinguir el tipo concreto; cada clase responde a su manera.
Un valor invalido devuelve `400` (ver "Errores de validacion").

## Entidades y comportamientos

`Entidad` es la clase abstracta base. Contiene los atributos privados `vida`, `danoBase` y
`velocidad`, valida sus valores en el constructor y declara el metodo abstracto
`describirComportamiento()`.

- `Player`: explora el mundo y combate a las entidades.
- `Zombie`: persigue y ataca al jugador si es hostil; de lo contrario, deambula sin atacar.
- `Aldeano`: comercia con el jugador si tiene comercializacion habilitada.
- `Cerdo`: puede ser montado si tiene esa capacidad habilitada.
- `Esqueleto`: ataca con arco o cuerpo a cuerpo segun su configuracion.
- `ZombiePequeño`: hereda el comportamiento del `Zombie` y agrega una velocidad aumentada.

El estado de las entidades permanece encapsulado: los atributos son privados, no existen
setters publicos y los constructores rechazan valores invalidos.

## Sobrecarga y sobreescritura

**Sobreescritura (override)**: `Entidad` declara `describirComportamiento()` como abstracto y
`Player`, `Zombie`, `Cerdo`, `Aldeano` y `Esqueleto` lo reimplementan con `@Override`, con la
misma firma. Java elige la version segun la clase real del objeto, en tiempo de ejecucion.

**Sobrecarga (overload)**: `Esqueleto` define dos metodos llamados `disparar` con distinta lista
de argumentos: `disparar()` y `disparar(int distancia)`. El compilador elige la version segun
los argumentos de la llamada. La segunda valida que la distancia no sea negativa.
Los constructores de cada clase tambien estan sobrecargados (sin parametros y con parametros).

**Como distinguirlas**: la sobreescritura mantiene la firma y cambia la clase (padre -> hija);
la sobrecarga mantiene el nombre y cambia los parametros dentro de la misma clase.

## Diagrama de clases

```mermaid
classDiagram
    class Entidad {
        <<abstract>>
        -int vida
        -int danoBase
        -int velocidad
        #Entidad()
        #Entidad(int vida, int danoBase, int velocidad)
        +int getVida()
        +int getDanoBase()
        +int getVelocidad()
        +String describirComportamiento()*
    }

    class Player {
        +Player()
        +Player(int vida, int danoBase, int velocidad)
        +String describirComportamiento()
    }

    class Zombie {
        -boolean hostilidad
        +Zombie()
        +Zombie(int vida, int danoBase, int velocidad, boolean hostilidad)
        +boolean isHostilidad()
        +String describirComportamiento()
    }

    class Cerdo {
        -boolean hostilidad
        -boolean montable
        +Cerdo()
        +Cerdo(int vida, int danoBase, int velocidad, boolean hostilidad, boolean montable)
        +boolean isHostilidad()
        +boolean isMontable()
        +String describirComportamiento()
    }

    class Aldeano {
        -boolean comercializacion
        +Aldeano()
        +Aldeano(int vida, int danoBase, int velocidad, boolean comercializacion)
        +boolean isComercializacion()
        +String describirComportamiento()
    }

    class Esqueleto {
        -boolean usaArco
        +Esqueleto()
        +Esqueleto(int vida, int danoBase, int velocidad, boolean usaArco)
        +boolean isUsaArco()
        +String disparar()
        +String disparar(int distancia)
        +String describirComportamiento()
    }

    class ZombiePequeño {
        -int velocidadAumentada
        +ZombiePequeño()
        +ZombiePequeño(int vida, int danoBase, int velocidad, boolean hostilidad, int velocidadAumentada)
        +int getVelocidadAumentada()
    }

    Entidad <|-- Player
    Entidad <|-- Zombie
    Entidad <|-- Cerdo
    Entidad <|-- Aldeano
    Entidad <|-- Esqueleto
    Zombie <|-- ZombiePequeño
```
