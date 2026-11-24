package mr_taller_git_2026;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;
import mr_taller_git_2026.service.SimulacionJugadorService;
import mr_taller_git_2026.service.cliente.ClienteSantiago;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class SimulacionJugadorTests {

    @Test
    void usaLaSaludYLaDistanciaDevueltasPorA2() {
        var respuesta = new AtomicReference<String>();
        var builder = WebClient.builder().exchangeFunction(request -> {
            assertEquals("/jugador", request.url().getPath());
            assertEquals("saludMaxima=20&x=3.0&y=0.0&z=0.0", request.url().getQuery());
            return Mono.just(ClientResponse.create(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(respuesta.get()).build());
        });
        var servicio = new SimulacionJugadorService(new ClienteSantiago(builder, "http://localhost", "Santiago Baez"));
        for (double distancia : new double[]{3, 3.01}) {
            respuesta.set("""
                    {"id":"Steve","salud":7,"saludMaxima":20,"posicion":{"x":%s,"y":0,"z":0}}
                    """.formatted(distancia));
            var impacto = servicio.simular(20, 3, 0, 0);
            assertEquals(distancia, impacto.distancia());
            assertEquals(7, impacto.vidaAnterior());
            assertEquals(distancia == 3 ? 7 : 0, impacto.danoRecibido());
            assertEquals(distancia == 3 ? 0 : 7, impacto.vidaFinal());
        }
    }
}
