package mr_taller_git_2026.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

/**
 * Explosión en cadena aceptada por A2 y A3: el Creeper local explota en el origen, alcanza al
 * jugador que validó A2 y detona una TNT que daña a la entidad de A3. Solo guarda el resultado;
 * las reglas las aplica {@code EscenarioService}.
 */
@Entity
public class Jugada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long creeperId;
    private String jugadorA2;
    private double distanciaJugador;
    private int saludJugadorAntes;
    private int danoCreeper;
    private int saludJugadorDespues;
    private String entidadA3;
    private int saludEntidadAntes;
    private int danoTnt;
    private int saludEntidadDespues;
    private Instant registradaEn;

    protected Jugada() {
    }

    public Jugada(Long creeperId, String jugadorA2, double distanciaJugador, int saludJugadorAntes,
            int danoCreeper, int saludJugadorDespues, String entidadA3, int saludEntidadAntes,
            int danoTnt, int saludEntidadDespues, Instant registradaEn) {
        this.creeperId = creeperId;
        this.jugadorA2 = jugadorA2;
        this.distanciaJugador = distanciaJugador;
        this.saludJugadorAntes = saludJugadorAntes;
        this.danoCreeper = danoCreeper;
        this.saludJugadorDespues = saludJugadorDespues;
        this.entidadA3 = entidadA3;
        this.saludEntidadAntes = saludEntidadAntes;
        this.danoTnt = danoTnt;
        this.saludEntidadDespues = saludEntidadDespues;
        this.registradaEn = registradaEn;
    }

    public Long getId() { return id; }
    public Long getCreeperId() { return creeperId; }
    public String getJugadorA2() { return jugadorA2; }
    public double getDistanciaJugador() { return distanciaJugador; }
    public int getSaludJugadorAntes() { return saludJugadorAntes; }
    public int getDanoCreeper() { return danoCreeper; }
    public int getSaludJugadorDespues() { return saludJugadorDespues; }
    public String getEntidadA3() { return entidadA3; }
    public int getSaludEntidadAntes() { return saludEntidadAntes; }
    public int getDanoTnt() { return danoTnt; }
    public int getSaludEntidadDespues() { return saludEntidadDespues; }
    public Instant getRegistradaEn() { return registradaEn; }
}
