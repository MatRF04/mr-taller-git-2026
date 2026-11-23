package mr_taller_git_2026.service;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import mr_taller_git_2026.domain.exception.CreeperYaExplotoException;
import mr_taller_git_2026.domain.exception.DistanciaInvalidaException;
import mr_taller_git_2026.domain.exception.EntidadMuertaException;
import mr_taller_git_2026.domain.exception.ObjetivoDuplicadoException;
import mr_taller_git_2026.domain.exception.TipoEntidadInvalidoException;
import mr_taller_git_2026.domain.*;
import mr_taller_git_2026.rest.dto.ExplosionRequest;
import mr_taller_git_2026.rest.dto.ExplosionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Valida y aplica la explosión en una transacción. */
@Service
public class ExplosionService {
    private static final int RADIO_EXPLOSION = 3;
    private static final int DANO_EXPLOSION = 10;

    private final EntidadService entidades;

    public ExplosionService(EntidadService entidades) {
        this.entidades = entidades;
    }

    @Transactional
    public ExplosionResponse explotar(Long id, ExplosionRequest datos) {
        Entidad origen = entidades.obtener(id);
        if (!(origen instanceof Creeper creeper)) {
            throw new TipoEntidadInvalidoException("El origen debe ser un Creeper");
        }
        if (datos == null || datos.objetivos() == null) {
            throw new IllegalArgumentException("objetivos debe contener al menos un objetivo");
        }
        List<ObjetivoExplosion> objetivos = new ArrayList<>();
        for (var objetivo : datos.objetivos()) {
            if (objetivo == null || objetivo.id() == null) {
                throw new IllegalArgumentException("Cada objetivo debe tener un id");
            }
            objetivos.add(new ObjetivoExplosion(entidades.obtener(objetivo.id()), objetivo.distancia()));
        }
        List<ExplosionResponse.Afectado> afectados = aplicarExplosion(creeper, objetivos).stream()
                .map(impacto -> new ExplosionResponse.Afectado(impacto.entidad().getId(),
                        impacto.entidad().getClass().getSimpleName(),
                        impacto.danoRecibido(), impacto.vidaFinal()))
                .toList();
        // JPA persiste las entidades administradas al confirmar esta transacción.
        return new ExplosionResponse(creeper.getId(), creeper.getVida(), afectados);
    }

    private List<Impacto> aplicarExplosion(Creeper creeper, List<ObjetivoExplosion> objetivos) {
        validarPuedeExplotar(creeper);
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
                throw new EntidadMuertaException("El objetivo está muerto: " + entidad.getId());
            }
        }
        creeper.registrarExplosion();
        List<Impacto> impactos = new ArrayList<>();
        for (ObjetivoExplosion objetivo : objetivos) {
            Entidad entidad = objetivo.entidad();
            int vidaAnterior = entidad.getVida();
            entidad.registrarVida(vidaTrasExplosion(vidaAnterior, objetivo.distancia()));
            impactos.add(new Impacto(entidad, vidaAnterior - entidad.getVida(), entidad.getVida()));
        }
        return impactos;
    }

    /** Regla 2: 10 puntos de daño hasta 3 bloques de distancia; fuera del radio, nada. */
    static int vidaTrasExplosion(int vida, double distancia) {
        return distancia <= RADIO_EXPLOSION ? Math.max(0, vida - DANO_EXPLOSION) : vida;
    }

    private void validarPuedeExplotar(Creeper creeper) {
        if (creeper.isYaExploto()) throw new CreeperYaExplotoException("El creeper ya explotó");
        if (!creeper.estaViva()) throw new EntidadMuertaException("El creeper está muerto");
    }

    private static void validarDistancia(Integer distancia) {
        if (distancia == null || distancia < 0) {
            throw new DistanciaInvalidaException("distancia es obligatoria y no puede ser negativa");
        }
    }

}
