package mr_taller_git_2026;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import mr_taller_git_2026.domain.exception.CompaneroNoDisponibleException;
import mr_taller_git_2026.domain.exception.RechazoA2Exception;
import mr_taller_git_2026.domain.exception.RechazoA3Exception;
import mr_taller_git_2026.service.cliente.ClienteLucas;
import mr_taller_git_2026.service.cliente.ClienteSantiago;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class ClientesCompanerosTests {

    private static WebClient.Builder responde(HttpStatus estado, String cuerpo,
            AtomicReference<ClientRequest> pedido) {
        return WebClient.builder().exchangeFunction(request -> {
            pedido.set(request);
            return Mono.just(ClientResponse.create(estado)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json").body(cuerpo).build());
        });
    }

    @Test
    void a2DevuelveElJugadorValidadoConSuPosicion() {
        var pedido = new AtomicReference<ClientRequest>();
        var a2 = new ClienteSantiago(responde(HttpStatus.OK, """
                {"id":"Steve","salud":20,"saludMaxima":20,"posicion":{"x":2.0,"y":0.0,"z":0.0},"experiencia":0}
                """, pedido), "http://a2", "Santiago Baez");

        var jugador = a2.obtenerJugador("Steve", 20, 2, 0, 0);

        assertEquals("/jugador", pedido.get().url().getPath());
        assertEquals("id=Steve&saludMaxima=20&x=2.0&y=0.0&z=0.0", pedido.get().url().getQuery());
        assertEquals("Steve", jugador.id());
        assertEquals(20, jugador.salud());
        assertEquals(2.0, jugador.x());
        assertEquals("A2 (Santiago Baez)", a2.getCompanero());
    }

    @Test
    void unErrorDeA2EsUnRechazoAtribuidoASantiago() {
        var a2 = new ClienteSantiago(responde(HttpStatus.BAD_REQUEST,
                "La salud máxima debe ser mayor a 0, se recibió: -1", new AtomicReference<>()),
                "http://a2", "Santiago Baez");

        var rechazo = assertThrows(RechazoA2Exception.class, () -> a2.obtenerJugador("Steve", -1, 0, 0, 0));

        assertEquals("A2 (Santiago Baez)", rechazo.getCompanero());
        assertEquals(400, rechazo.getEstadoRemoto());
        assertTrue(rechazo.getDetalleRemoto().contains("salud máxima"));
    }

    @Test
    void a3SimulaLaTntConElCuerpoDeSuOpenApi() {
        var pedido = new AtomicReference<ClientRequest>();
        var a3 = new ClienteLucas(responde(HttpStatus.OK, """
                {"id":"jugador1","saludAnterior":20,"saludActual":12,"danoAplicado":8,"radio":4.0}
                """, pedido), "http://a3", "Lucas Fadul");

        var simulacion = a3.simularTnt("jugador1", "tnt-1");

        assertEquals(HttpMethod.POST, pedido.get().method());
        assertEquals("/api/entidades/jugador1/explosiones", pedido.get().url().getPath());
        assertEquals(8, simulacion.danoAplicado());
        assertEquals(12, simulacion.saludActual());
    }

    @Test
    void unConflictoDeA3EsUnRechazoAtribuidoALucas() {
        var a3 = new ClienteLucas(responde(HttpStatus.CONFLICT, "{\"error\":\"El objetivo no tiene vida\"}",
                new AtomicReference<>()), "http://a3", "Lucas Fadul");

        var rechazo = assertThrows(RechazoA3Exception.class, () -> a3.simularTnt("muerto", "tnt-1"));

        assertEquals("A3 (Lucas Fadul)", rechazo.getCompanero());
        assertEquals(409, rechazo.getEstadoRemoto());
    }

    @Test
    void unFalloDelServidorNoEsUnRechazoSinoUnaCaida() {
        var a3 = new ClienteLucas(responde(HttpStatus.INTERNAL_SERVER_ERROR, "{}", new AtomicReference<>()),
                "http://a3", "Lucas Fadul");

        var caida = assertThrows(CompaneroNoDisponibleException.class, () -> a3.simularTnt("jugador1", "tnt-1"));

        assertEquals("A3 (Lucas Fadul)", caida.getCompanero());
    }

    @Test
    void siA2NoRespondeATiempoSeInformaQueNoEstaDisponible() {
        var sinRespuesta = WebClient.builder().exchangeFunction(request ->
                Mono.<ClientResponse>never().timeout(Duration.ofMillis(10)));
        var a2 = new ClienteSantiago(sinRespuesta, "http://a2", "Santiago Baez");

        var caida = assertThrows(CompaneroNoDisponibleException.class, () -> a2.obtenerJugador("Steve", 20, 0, 0, 0));

        assertEquals("A2 (Santiago Baez)", caida.getCompanero());
    }
}
