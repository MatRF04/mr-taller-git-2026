package mr_taller_git_2026;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mr_taller_git_2026.domain.Creeper;
import mr_taller_git_2026.domain.exception.CompaneroNoDisponibleException;
import mr_taller_git_2026.domain.exception.RechazoA2Exception;
import mr_taller_git_2026.domain.exception.RechazoA3Exception;
import mr_taller_git_2026.repository.EntidadRepository;
import mr_taller_git_2026.repository.JugadaRepository;
import mr_taller_git_2026.service.cliente.ClienteLucas;
import mr_taller_git_2026.service.cliente.ClienteLucas.ExplosionTntA3;
import mr_taller_git_2026.service.cliente.ClienteSantiago;
import mr_taller_git_2026.service.cliente.ClienteSantiago.JugadorA2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class EscenarioTests {
    private static final String A2 = "A2 (Santiago Baez)";
    private static final String A3 = "A3 (Lucas Fadul)";

    @Autowired MockMvc mvc;
    @Autowired EntidadRepository entidades;
    @Autowired JugadaRepository jugadas;
    @MockitoBean ClienteSantiago a2;
    @MockitoBean ClienteLucas a3;

    @BeforeEach
    void limpiar() {
        jugadas.deleteAll();
    }

    private ResultActions jugar(Long creeperId) throws Exception {
        return mvc.perform(post("/jugadas").contentType(MediaType.APPLICATION_JSON).content("""
                {"creeperId":%d,
                 "jugadorA2":{"id":"Steve","saludMaxima":20,"x":2,"y":0,"z":0},
                 "entidadA3":{"id":"jugador1"}}
                """.formatted(creeperId)));
    }

    private boolean exploto(Long id) {
        return ((Creeper) entidades.findById(id).orElseThrow()).isYaExploto();
    }

    @Test
    void siA2YA3AceptanLaJugadaSeGuardaYSePuedeLeer() throws Exception {
        var creeper = entidades.save(new Creeper(20, 5, 3));
        when(a2.obtenerJugador("Steve", 20, 2.0, 0.0, 0.0)).thenReturn(new JugadorA2("Steve", 20, 20, 2, 0, 0));
        when(a3.explotarTnt("jugador1", 0, 0, 0)).thenReturn(new ExplosionTntA3("jugador1", 20, 12, 8));

        String ubicacion = jugar(creeper.getId()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.jugadorA2").value("Steve"))
                .andExpect(jsonPath("$.distanciaJugador").value(2.0))
                .andExpect(jsonPath("$.danoCreeper").value(10))
                .andExpect(jsonPath("$.saludJugadorDespues").value(10))
                .andExpect(jsonPath("$.danoTnt").value(8))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get(ubicacion)).andExpect(status().isOk())
                .andExpect(jsonPath("$.creeperId").value(creeper.getId()))
                .andExpect(jsonPath("$.entidadA3").value("jugador1"))
                .andExpect(jsonPath("$.saludEntidadDespues").value(12));
        assertTrue(exploto(creeper.getId()));
        jugar(creeper.getId()).andExpect(status().isConflict());
    }

    @Test
    void siA2RechazaLaBaseQuedaIgualYSeIndicaQueFueA2() throws Exception {
        var creeper = entidades.save(new Creeper(20, 5, 3));
        when(a2.obtenerJugador(anyString(), anyInt(), anyDouble(), anyDouble(), anyDouble()))
                .thenThrow(new RechazoA2Exception(A2, 400, "La salud máxima debe ser mayor a 0", null));

        jugar(creeper.getId()).andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.rechazadoPor").value(A2))
                .andExpect(jsonPath("$.estadoRemoto").value(400))
                .andExpect(jsonPath("$.detalleRemoto").value("La salud máxima debe ser mayor a 0"));

        assertEquals(0, jugadas.count());
        assertFalse(exploto(creeper.getId()));
        verify(a3, never()).explotarTnt(any(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void siA3RechazaLaBaseQuedaIgualYSeIndicaQueFueA3() throws Exception {
        var creeper = entidades.save(new Creeper(20, 5, 3));
        when(a2.obtenerJugador(anyString(), anyInt(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(new JugadorA2("Steve", 20, 20, 2, 0, 0));
        when(a3.explotarTnt(anyString(), anyDouble(), anyDouble(), anyDouble()))
                .thenThrow(new RechazoA3Exception(A3, 409, "{\"error\":\"El objetivo no tiene vida\"}", null));

        jugar(creeper.getId()).andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.rechazadoPor").value(A3))
                .andExpect(jsonPath("$.estadoRemoto").value(409));

        assertEquals(0, jugadas.count());
        assertFalse(exploto(creeper.getId()));
        mvc.perform(get("/jugadas")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void siA3NoRespondeSeIndicaCualFallaYNoSeGuardaNada() throws Exception {
        var creeper = entidades.save(new Creeper(20, 5, 3));
        when(a2.obtenerJugador(anyString(), anyInt(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(new JugadorA2("Steve", 20, 20, 2, 0, 0));
        when(a3.explotarTnt(anyString(), anyDouble(), anyDouble(), anyDouble()))
                .thenThrow(new CompaneroNoDisponibleException(A3, "No se pudo consultar a " + A3, null));

        jugar(creeper.getId()).andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.servicio").value(A3));

        assertEquals(0, jugadas.count());
        assertFalse(exploto(creeper.getId()));
    }

    @Test
    void unCreeperQueYaExplotoSeRechazaSinConsultarALosCompaneros() throws Exception {
        var creeper = new Creeper(20, 5, 3);
        creeper.registrarExplosion();
        creeper = entidades.save(creeper);

        jugar(creeper.getId()).andExpect(status().isConflict());

        verify(a2, never()).obtenerJugador(any(), anyInt(), anyDouble(), anyDouble(), anyDouble());
        assertEquals(0, jugadas.count());
    }

    @Test
    void unaJugadaInexistenteResponde404() throws Exception {
        mvc.perform(get("/jugadas/9999")).andExpect(status().isNotFound());
    }
}
