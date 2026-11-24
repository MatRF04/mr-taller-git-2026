package mr_taller_git_2026.service.cliente;

import java.util.Map;
import mr_taller_git_2026.domain.exception.RechazoA3Exception;
import mr_taller_git_2026.domain.exception.RechazoCompaneroException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A3: servicio de Lucas Fadul. {@code POST /api/entidades/{id}/explosiones} con
 * {@code {"posicion": {x, y, z}}} aplica su regla: una TNT causa hasta 8 de daño a una entidad
 * viva dentro de 4 bloques, y fuera del radio no daña. Responde 404 si la entidad no existe y
 * 409 si no tiene vida.
 *
 * <p>Esta llamada <b>modifica la base de A3</b> (guarda la nueva salud de su entidad); por eso el
 * escenario la hace al final, cuando las demás validaciones ya aceptaron.
 */
@Component
public class ClienteLucas extends ClienteCompanero {

    public ClienteLucas(WebClient.Builder builder, @Value("${companeros.a3.url}") String url,
            @Value("${companeros.a3.nombre}") String nombre) {
        super(builder, url, "A3 (" + nombre + ")");
    }

    public ExplosionTntA3 explotarTnt(String entidadId, double x, double y, double z) {
        Explosion explosion = esperar(cliente.post()
                .uri("/api/entidades/{id}/explosiones", entidadId)
                .bodyValue(Map.of("posicion", Map.of("x", x, "y", y, "z", z)))
                .retrieve()
                .bodyToMono(Explosion.class));
        if (explosion.saludAnterior() == null || explosion.saludActual() == null
                || explosion.danoAplicado() == null) {
            throw noDisponible(getCompanero() + " devolvió una explosión incompleta", null);
        }
        return new ExplosionTntA3(entidadId, explosion.saludAnterior(), explosion.saludActual(),
                explosion.danoAplicado());
    }

    @Override
    protected RechazoCompaneroException rechazo(int estado, String detalle, Throwable causa) {
        return new RechazoA3Exception(getCompanero(), estado, detalle, causa);
    }

    /** Resultado de la regla de A3 sobre su entidad. */
    public record ExplosionTntA3(String entidadId, int saludAnterior, int saludActual, int danoAplicado) {}

    private record Explosion(Integer saludAnterior, Integer saludActual, Integer danoAplicado) {}
}
