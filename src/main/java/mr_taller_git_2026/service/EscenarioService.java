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
 * Escenario de POO-09: explosión en cadena. El Creeper local explota en el origen, alcanza al
 * jugador que valida A2 y detona una TNT en el mismo lugar, que daña a la entidad de A3.
 *
 * <p>Orden: primero las reglas locales (no tiene sentido molestar a los compañeros con un
 * Creeper que no puede explotar), después A2 (su consulta no cambia nada y su posición hace
 * falta para calcular el daño del Creeper) y por último A3, porque es la única llamada que
 * modifica una base ajena: si algo antes rechaza, a A3 no se lo llama. Solo cuando las dos
 * aceptan se escribe en H2; si una rechaza, la excepción revierte la transacción antes de
 * cualquier cambio y el handler indica de quién fue el rechazo.
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
        var jugador = a2.obtenerJugador(pedidoA2.id(), pedidoA2.saludMaxima(),
                pedidoA2.x(), pedidoA2.y(), pedidoA2.z());

        var tnt = a3.explotarTnt(datos.entidadA3().id(), 0, 0, 0);

        double distancia = SimulacionJugadorService.distanciaAlOrigen(jugador.x(), jugador.y(), jugador.z());
        int saludDespues = ExplosionService.vidaTrasExplosion(jugador.salud(), distancia);
        creeper.registrarExplosion();
        return jugadas.save(new Jugada(creeper.getId(), jugador.id(), distancia, jugador.salud(),
                jugador.salud() - saludDespues, saludDespues, tnt.entidadId(), tnt.saludAnterior(),
                tnt.danoAplicado(), tnt.saludActual(), Instant.now()));
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
        if (jugador == null || jugador.saludMaxima() == null || jugador.x() == null
                || jugador.y() == null || jugador.z() == null) {
            throw new IllegalArgumentException("jugadorA2 necesita saludMaxima, x, y y z");
        }
        SimulacionJugadorService.validarCoordenadas(jugador.x(), jugador.y(), jugador.z());
        if (datos.entidadA3() == null || esVacio(datos.entidadA3().id())) {
            throw new IllegalArgumentException("entidadA3 necesita el id de la entidad de A3");
        }
    }

    private static boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
