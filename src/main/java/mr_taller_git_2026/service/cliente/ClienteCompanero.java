package mr_taller_git_2026.service.cliente;

import java.time.Duration;
import mr_taller_git_2026.domain.exception.CompaneroException;
import mr_taller_git_2026.domain.exception.CompaneroNoDisponibleException;
import mr_taller_git_2026.domain.exception.RechazoCompaneroException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Cliente HTTP hacia la API de un compañero. Las rutas y los cuerpos salen de su OpenAPI;
 * esta clase solo traduce sus errores: un 4xx es un rechazo de su regla, y un timeout,
 * un 5xx o una respuesta incompleta significan que su servicio no está disponible.
 */
public abstract class ClienteCompanero {
    private static final Duration ESPERA_MAXIMA = Duration.ofSeconds(5);

    protected final WebClient cliente;
    private final String companero;

    protected ClienteCompanero(WebClient.Builder builder, String url, String companero) {
        this.cliente = builder.clone().baseUrl(url).build();
        this.companero = companero;
    }

    /** Rol y nombre del compañero, por ejemplo "A2 (Santiago Baez)". */
    public String getCompanero() {
        return companero;
    }

    protected abstract RechazoCompaneroException rechazo(int estado, String detalle, Throwable causa);

    protected <T> T esperar(Mono<T> pedido) {
        T respuesta;
        try {
            respuesta = pedido.timeout(ESPERA_MAXIMA).block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw rechazo(e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            }
            throw noDisponible(companero + " respondió " + e.getStatusCode().value(), e);
        } catch (CompaneroException e) {
            throw e;
        } catch (RuntimeException e) {
            throw noDisponible("No se pudo consultar a " + companero, e);
        }
        if (respuesta == null) {
            throw noDisponible(companero + " respondió sin cuerpo", null);
        }
        return respuesta;
    }

    protected CompaneroNoDisponibleException noDisponible(String mensaje, Throwable causa) {
        return new CompaneroNoDisponibleException(companero, mensaje, causa);
    }
}
