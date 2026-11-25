package mr_taller_git_2026.service.cliente;

import mr_taller_git_2026.domain.exception.RechazoA2Exception;
import mr_taller_git_2026.domain.exception.RechazoCompaneroException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A2: servicio de Santiago Baez. Según su OpenAPI, {@code GET /jugador} valida los parámetros
 * (la salud máxima tiene que ser mayor que 0 y la salud quedar entre 0 y la máxima) y construye
 * el jugador sin guardarlo. Responde 400 o 422 si la regla no se cumple.
 */
@Component
public class ClienteSantiago extends ClienteCompanero {

    public ClienteSantiago(WebClient.Builder builder, @Value("${companeros.a2.url}") String url,
            @Value("${companeros.a2.nombre}") String nombre) {
        super(builder, url, "A2 (" + nombre + ")");
    }

    public JugadorA2 obtenerJugador(String id, int saludMaxima, double x, double y, double z) {
        Jugador jugador = esperar(cliente.get()
                .uri(uri -> uri.path("/jugador")
                        .queryParamIfPresent("id", java.util.Optional.ofNullable(id))
                        .queryParam("saludMaxima", saludMaxima)
                        .queryParam("x", x).queryParam("y", y).queryParam("z", z)
                        .build())
                .retrieve()
                .bodyToMono(Jugador.class));
        Posicion p = jugador.posicion();
        if (jugador.id() == null || jugador.salud() == null || jugador.saludMaxima() == null || p == null
                || p.x() == null || p.y() == null || p.z() == null) {
            throw noDisponible(getCompanero() + " devolvió un jugador incompleto", null);
        }
        return new JugadorA2(jugador.id(), jugador.salud(), jugador.saludMaxima(), p.x(), p.y(), p.z());
    }

    /**
     * Regla de A2: {@code POST /simulacion/explosiones}. El daño de la explosión decae con la
     * distancia, {@code max(1, round(20 * (1 - distancia / radio)))} dentro del radio. Responde
     * 404 si el creeper o el jugador no existen en su base y 422 si se rompe una regla (por
     * ejemplo, un radio que no es positivo). Guarda la nueva salud del jugador en la base de A2.
     */
    public ExplosionA2 explotar(String creeperId, double radio, String jugadorId) {
        Simulacion simulacion = esperar(cliente.post()
                .uri("/simulacion/explosiones")
                .bodyValue(java.util.Map.of("creeperId", creeperId, "radio", radio,
                        "jugadores", java.util.List.of(java.util.Map.of("id", jugadorId)),
                        "bloques", java.util.List.of()))
                .retrieve()
                .bodyToMono(Simulacion.class));
        Afectado afectado = simulacion.jugadores() == null ? null : simulacion.jugadores().stream()
                .filter(j -> jugadorId.equals(j.id())).findFirst().orElse(null);
        if (afectado == null || afectado.distancia() == null || afectado.danio() == null
                || afectado.saludAntes() == null || afectado.saludDespues() == null) {
            throw noDisponible(getCompanero() + " devolvió una explosión incompleta", null);
        }
        return new ExplosionA2(jugadorId, afectado.distancia(), afectado.saludAntes(), afectado.danio(),
                afectado.saludDespues());
    }

    /** Resultado de la regla de daño de A2 sobre su jugador. */
    public record ExplosionA2(String jugadorId, double distancia, int saludAntes, int danio, int saludDespues) {}

    private record Simulacion(java.util.List<Afectado> jugadores) {}

    private record Afectado(String id, Double distancia, Integer danio, Integer saludAntes, Integer saludDespues) {}

    @Override
    protected RechazoCompaneroException rechazo(int estado, String detalle, Throwable causa) {
        return new RechazoA2Exception(getCompanero(), estado, detalle, causa);
    }

    /** Jugador validado por A2. */
    public record JugadorA2(String id, int salud, int saludMaxima, double x, double y, double z) {}

    private record Jugador(String id, Integer salud, Integer saludMaxima, Posicion posicion) {}

    private record Posicion(Double x, Double y, Double z) {}
}
