package mr_taller_git_2026.service.cliente;

import java.util.Map;
import mr_taller_git_2026.domain.exception.RechazoA3Exception;
import mr_taller_git_2026.domain.exception.RechazoCompaneroException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A3: servicio de Lucas Fadul. Según su OpenAPI, {@code POST /api/entidades/{id}/explosiones}
 * simula su regla: una TNT causa hasta 8 de daño dentro de 4 bloques y fuera del radio no daña.
 * Responde 404 si la TNT o el objetivo no existen y 409 si el objetivo no tiene vida.
 */
@Component
public class ClienteLucas extends ClienteCompanero {

    public ClienteLucas(WebClient.Builder builder, @Value("${companeros.a3.url}") String url,
            @Value("${companeros.a3.nombre}") String nombre) {
        super(builder, url, "A3 (" + nombre + ")");
    }

    public SimulacionTntA3 simularTnt(String objetivoId, String tntId) {
        Simulacion simulacion = esperar(cliente.post()
                .uri("/api/entidades/{id}/explosiones", objetivoId)
                .bodyValue(Map.of("tntId", tntId, "objetivoId", objetivoId))
                .retrieve()
                .bodyToMono(Simulacion.class));
        if (simulacion.saludAnterior() == null || simulacion.saludActual() == null
                || simulacion.danoAplicado() == null) {
            throw noDisponible(getCompanero() + " devolvió una simulación incompleta", null);
        }
        return new SimulacionTntA3(objetivoId, tntId, simulacion.saludAnterior(),
                simulacion.saludActual(), simulacion.danoAplicado());
    }

    @Override
    protected RechazoCompaneroException rechazo(int estado, String detalle, Throwable causa) {
        return new RechazoA3Exception(getCompanero(), estado, detalle, causa);
    }

    /** Resultado de la regla de A3 sobre su objetivo. */
    public record SimulacionTntA3(String objetivoId, String tntId, int saludAnterior, int saludActual,
            int danoAplicado) {}

    private record Simulacion(Integer saludAnterior, Integer saludActual, Integer danoAplicado) {}
}
