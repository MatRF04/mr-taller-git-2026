package mr_taller_git_2026.service;

import java.time.Instant;
import java.util.List;
import mr_taller_git_2026.domain.Creeper;
import mr_taller_git_2026.domain.Entidad;
import mr_taller_git_2026.domain.Jugada;
import mr_taller_git_2026.domain.exception.JugadaNoEncontradaException;
import mr_taller_git_2026.domain.exception.TipoEntidadInvalidoException;
import mr_taller_git_2026.repository.JugadaRepository;
import mr_taller_git_2026.rest.dto.JugadaRequest;
import mr_taller_git_2026.service.cliente.ClienteLucas;
import mr_taller_git_2026.service.cliente.ClienteSantiago;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Escenario de POO-09: explosión en cadena. El Creeper local explota y detona el creeper de A2,
 * cuya explosión daña a su jugador según la regla de daño de A2 (decae con la distancia), y una
 * TNT que daña a la entidad de A3 según la regla de A3.
 *
 * <p>Orden: primero las reglas locales (no tiene sentido molestar a los compañeros con un
 * Creeper que no puede explotar), después A2 y por último A3. Solo cuando las dos aceptan se
 * escribe en H2; si una rechaza, la excepción revierte la transacción antes de cualquier cambio
 * local y el handler indica de quién fue el rechazo. Las reglas de A2 y A3 guardan su resultado
 * en sus propias bases: si A2 acepta y A3 rechaza, el cambio en A2 no se deshace.
 */
@Service
public class EscenarioService {
    private final EntidadService entidades;
    private final ExplosionService explosiones;
    private final ClienteSantiago a2;
    private final ClienteLucas a3;
    private final JugadaRepository jugadas;

    public EscenarioService(EntidadService entidades, ExplosionService explosiones, ClienteSantiago a2,
            ClienteLucas a3, JugadaRepository jugadas) {
        this.entidades = entidades;
        this.explosiones = explosiones;
        this.a2 = a2;
        this.a3 = a3;
        this.jugadas = jugadas;
    }

    @Transactional
    public Jugada jugar(JugadaRequest datos) {
        validar(datos);
        Creeper creeper = creeper(datos.creeperId());

        var pedidoA2 = datos.jugadorA2();
        var explosionA2 = a2.explotar(pedidoA2.creeperId(), pedidoA2.radio(), pedidoA2.id());

        var tnt = a3.explotarTnt(datos.entidadA3().id(), 0, 0, 0);

        creeper.registrarExplosion();
        return jugadas.save(new Jugada(creeper.getId(), explosionA2.jugadorId(), explosionA2.distancia(),
                explosionA2.saludAntes(), explosionA2.danio(), explosionA2.saludDespues(),
                tnt.entidadId(), tnt.saludAnterior(), tnt.danoAplicado(), tnt.saludActual(), Instant.now()));
    }

    @Transactional(readOnly = true)
    public List<Jugada> listar() {
        return jugadas.findAll();
    }

    @Transactional(readOnly = true)
    public Jugada obtener(Long id) {
        return jugadas.findById(id).orElseThrow(() -> new JugadaNoEncontradaException(id));
    }

    private Creeper creeper(Long id) {
        Entidad origen = entidades.obtener(id);
        if (!(origen instanceof Creeper creeper)) {
            throw new TipoEntidadInvalidoException("El origen debe ser un Creeper");
        }
        explosiones.validarPuedeExplotar(creeper);
        return creeper;
    }

    private static void validar(JugadaRequest datos) {
        if (datos == null || datos.creeperId() == null) {
            throw new IllegalArgumentException("creeperId es obligatorio");
        }
        var jugador = datos.jugadorA2();
        if (jugador == null || esVacio(jugador.id()) || esVacio(jugador.creeperId()) || jugador.radio() == null) {
            throw new IllegalArgumentException("jugadorA2 necesita id, creeperId y radio");
        }
        if (datos.entidadA3() == null || esVacio(datos.entidadA3().id())) {
            throw new IllegalArgumentException("entidadA3 necesita el id de la entidad de A3");
        }
    }

    private static boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
