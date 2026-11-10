package mr_taller_git_2026.service;

import java.util.ArrayList;
import java.util.List;
import mr_taller_git_2026.domain.*;
import mr_taller_git_2026.domain.exception.TipoEntidadInvalidoException;
import mr_taller_git_2026.rest.dto.ExplosionRequest;
import mr_taller_git_2026.rest.dto.ExplosionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Carga los participantes y delega las reglas en Creeper; no decide dano ni validez. */
@Service
public class ExplosionService {
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
        List<ExplosionResponse.Afectado> afectados = creeper.explotar(objetivos).stream()
                .map(impacto -> new ExplosionResponse.Afectado(impacto.entidad().getId(),
                        impacto.entidad().getClass().getSimpleName(),
                        impacto.danoRecibido(), impacto.vidaFinal()))
                .toList();
        // JPA persiste las entidades administradas al confirmar esta transaccion.
        return new ExplosionResponse(creeper.getId(), creeper.getVida(), afectados);
    }
}
