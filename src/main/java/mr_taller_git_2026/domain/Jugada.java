package mr_taller_git_2026.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

/**
 * Explosión en cadena aceptada por A2 y A3: el Creeper local explota, alcanza al jugador que
 * validó A2 y detona la TNT de A3 sobre su objetivo. Solo guarda el resultado; las reglas
 * las aplica {@code EscenarioService}.
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
    private String tntA3;
    private String objetivoA3;
    private int saludObjetivoAntes;
    private int danoTnt;
    private int saludObjetivoDespues;
    private Instant registradaEn;

    protected Jugada() {
    }

    public Jugada(Long creeperId, String jugadorA2, double distanciaJugador, int saludJugadorAntes,
            int danoCreeper, int saludJugadorDespues, String tntA3, String objetivoA3,
            int saludObjetivoAntes, int danoTnt, int saludObjetivoDespues, Instant registradaEn) {
        this.creeperId = creeperId;
        this.jugadorA2 = jugadorA2;
        this.distanciaJugador = distanciaJugador;
        this.saludJugadorAntes = saludJugadorAntes;
        this.danoCreeper = danoCreeper;
        this.saludJugadorDespues = saludJugadorDespues;
        this.tntA3 = tntA3;
        this.objetivoA3 = objetivoA3;
        this.saludObjetivoAntes = saludObjetivoAntes;
        this.danoTnt = danoTnt;
        this.saludObjetivoDespues = saludObjetivoDespues;
        this.registradaEn = registradaEn;
    }

    public Long getId() { return id; }
    public Long getCreeperId() { return creeperId; }
    public String getJugadorA2() { return jugadorA2; }
    public double getDistanciaJugador() { return distanciaJugador; }
    public int getSaludJugadorAntes() { return saludJugadorAntes; }
    public int getDanoCreeper() { return danoCreeper; }
    public int getSaludJugadorDespues() { return saludJugadorDespues; }
    public String getTntA3() { return tntA3; }
    public String getObjetivoA3() { return objetivoA3; }
    public int getSaludObjetivoAntes() { return saludObjetivoAntes; }
    public int getDanoTnt() { return danoTnt; }
    public int getSaludObjetivoDespues() { return saludObjetivoDespues; }
    public Instant getRegistradaEn() { return registradaEn; }
}
