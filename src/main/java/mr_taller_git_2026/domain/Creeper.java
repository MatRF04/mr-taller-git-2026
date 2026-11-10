package mr_taller_git_2026.domain;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import mr_taller_git_2026.domain.exception.CreeperYaExplotoException;
import mr_taller_git_2026.domain.exception.DistanciaInvalidaException;
import mr_taller_git_2026.domain.exception.EntidadMuertaException;
import mr_taller_git_2026.domain.exception.ObjetivoDuplicadoException;
import mr_taller_git_2026.domain.exception.TipoEntidadInvalidoException;

@jakarta.persistence.Entity
public class Creeper extends Entidad {
    static final int RADIO_EXPLOSION = 3;
    static final int DANO_EXPLOSION = 10;

    private boolean yaExploto;

    public Creeper() {
        super();
    }

    public Creeper(int vida, int danoBase, int velocidad) {
        super(vida, danoBase, velocidad);
    }

    public boolean isYaExploto() {
        return yaExploto;
    }

    /** Regla 1: un creeper vivo explota una sola vez y muere al hacerlo. */
    public void explotar() {
        validarPuedeExplotar();
        yaExploto = true;
        recibirDano(getVida());
    }

    /**
     * Explota sobre los objetivos. Valida todo antes de cambiar cualquier estado,
     * de modo que una regla incumplida no deja danos parciales.
     */
    public List<Impacto> explotar(List<ObjetivoExplosion> objetivos) {
        validarPuedeExplotar();
        if (objetivos == null || objetivos.isEmpty()) {
            throw new IllegalArgumentException("objetivos debe contener al menos un objetivo");
        }
        Map<Entidad, Boolean> vistos = new IdentityHashMap<>();
        for (ObjetivoExplosion objetivo : objetivos) {
            validarDistancia(objetivo.distancia());
            Entidad entidad = objetivo.entidad();
            if (vistos.put(entidad, true) != null) {
                throw new ObjetivoDuplicadoException("Objetivo repetido: " + entidad.getId());
            }
            if (!(entidad instanceof Player) && !(entidad instanceof Zombie)) {
                throw new TipoEntidadInvalidoException("El objetivo debe ser Player o Zombie");
            }
            if (!entidad.estaViva()) {
                throw new EntidadMuertaException("El objetivo esta muerto: " + entidad.getId());
            }
        }
        explotar();
        List<Impacto> impactos = new ArrayList<>();
        for (ObjetivoExplosion objetivo : objetivos) {
            Entidad entidad = objetivo.entidad();
            int vidaAnterior = entidad.getVida();
            entidad.recibirDano(danoExplosion(objetivo.distancia()));
            impactos.add(new Impacto(entidad, vidaAnterior - entidad.getVida(), entidad.getVida()));
        }
        return impactos;
    }

    /** Regla 2: 10 puntos de dano hasta 3 bloques de distancia; fuera del radio, nada. */
    public int danoExplosion(int distancia) {
        validarDistancia(distancia);
        return distancia <= RADIO_EXPLOSION ? DANO_EXPLOSION : 0;
    }

    private void validarPuedeExplotar() {
        if (yaExploto) throw new CreeperYaExplotoException("El creeper ya exploto");
        if (!estaViva()) throw new EntidadMuertaException("El creeper esta muerto");
    }

    private static void validarDistancia(Integer distancia) {
        if (distancia == null || distancia < 0) {
            throw new DistanciaInvalidaException("distancia es obligatoria y no puede ser negativa");
        }
    }

    @Override
    public String describirComportamiento() {
        return yaExploto ? "Ya exploto." : "Explota cerca de otras entidades.";
    }
}
