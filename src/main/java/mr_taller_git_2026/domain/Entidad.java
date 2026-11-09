package mr_taller_git_2026.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Jackson agrega "tipo" al serializar para que el cliente distinga el subtipo.
// La entrada se recibe con EntidadRequest: nunca se deserializa Entidad directo,
// porque Jackson asignaria los campos privados sin pasar por los constructores.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Creeper.class, name = "Creeper"),
        @JsonSubTypes.Type(value = Player.class, name = "Player"),
        @JsonSubTypes.Type(value = Zombie.class, name = "Zombie"),
        @JsonSubTypes.Type(value = ZombiePequeño.class, name = "ZombiePequeno"),
        @JsonSubTypes.Type(value = Esqueleto.class, name = "Esqueleto"),
        @JsonSubTypes.Type(value = Aldeano.class, name = "Aldeano"),
        @JsonSubTypes.Type(value = Cerdo.class, name = "Cerdo")
})
@jakarta.persistence.Entity
@jakarta.persistence.Inheritance(strategy = jakarta.persistence.InheritanceType.SINGLE_TABLE)
public abstract class Entidad {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public boolean estaViva() {
        return vida > 0;
    }

    public void recibirDano(int cantidad) {
        validarNoNegativo(cantidad, "cantidad");
        vida = Math.max(0, vida - cantidad);
    }

    public void actualizarAtributos(int vida, int danoBase, int velocidad) {
        validarVida(vida);
        validarNoNegativo(danoBase, "danoBase");
        validarNoNegativo(velocidad, "velocidad");
        this.vida = vida;
        this.danoBase = danoBase;
        this.velocidad = velocidad;
    }

    private int vida;
    private int danoBase;
    private int velocidad;

    protected Entidad() {
        this(1, 0, 0);
    }

    protected Entidad(int vida, int danoBase, int velocidad) {
        this.vida = validarVida(vida);
        this.danoBase = validarNoNegativo(danoBase, "danoBase");
        this.velocidad = validarNoNegativo(velocidad, "velocidad");
    }

    public abstract String describirComportamiento();

    protected static int validarVida(int vida) {
        if (vida <= 0) {
            throw new IllegalArgumentException("La vida debe ser mayor que cero");
        }
        return vida;
    }

    protected static int validarNoNegativo(int valor, String nombre) {
        if (valor < 0) {
            throw new IllegalArgumentException(nombre + " no puede ser negativo");
        }
        return valor;
    }

    public int getVida() {
        return vida;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public int getVelocidad() {
        return velocidad;
    }
}
