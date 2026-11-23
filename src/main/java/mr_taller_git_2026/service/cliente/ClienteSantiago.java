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

    @Override
    protected RechazoCompaneroException rechazo(int estado, String detalle, Throwable causa) {
        return new RechazoA2Exception(getCompanero(), estado, detalle, causa);
    }

    /** Jugador validado por A2. */
    public record JugadorA2(String id, int salud, int saludMaxima, double x, double y, double z) {}

    private record Jugador(String id, Integer salud, Integer saludMaxima, Posicion posicion) {}

    private record Posicion(Double x, Double y, Double z) {}
}
