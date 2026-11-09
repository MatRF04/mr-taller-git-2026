package mr_taller_git_2026;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import mr_taller_git_2026.domain.*;
import mr_taller_git_2026.repository.EntidadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ExplosionTests {
    @Autowired MockMvc mvc;
    @Autowired EntidadRepository repository;

    private Creeper creeper() { return repository.save(new Creeper(20, 5, 3)); }
    private Player player(int vida) { return repository.save(new Player(vida, 5, 3)); }
    private String objetivo(Long id, int distancia) {
        return "{\"id\":" + id + ",\"distancia\":" + distancia + "}";
    }
    private org.springframework.test.web.servlet.ResultActions explotar(Long id, String objetivos) throws Exception {
        return mvc.perform(post("/entidades/" + id + "/explotar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"objetivos\":[" + objetivos + "]}"));
    }

    @Test
    void explosionPersisteLosTresParticipantesYNoPermiteRepetir() throws Exception {
        var creeper = creeper();
        var player = player(20);
        var zombie = repository.save(new Zombie(7, 5, 3, true));
        explotar(creeper.getId(), objetivo(player.getId(), 0) + "," + objetivo(zombie.getId(), 3))
                .andExpect(status().isOk()).andExpect(jsonPath("$.vidaCreeper").value(0))
                .andExpect(jsonPath("$.afectados[0].danoRecibido").value(10))
                .andExpect(jsonPath("$.afectados[1].danoRecibido").value(7))
                .andExpect(jsonPath("$.afectados[1].vidaFinal").value(0));
        assertEquals(10, repository.findById(player.getId()).orElseThrow().getVida());
        assertEquals(0, repository.findById(zombie.getId()).orElseThrow().getVida());
        assertTrue(((Creeper) repository.findById(creeper.getId()).orElseThrow()).isYaExploto());
        explotar(creeper.getId(), objetivo(player.getId(), 1)).andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El creeper ya exploto"));
    }

    @Test
    void fueraDelRadioNoRecibeDano() throws Exception {
        var player = player(20);
        explotar(creeper().getId(), objetivo(player.getId(), 4)).andExpect(status().isOk())
                .andExpect(jsonPath("$.afectados[0].danoRecibido").value(0));
        assertEquals(20, repository.findById(player.getId()).orElseThrow().getVida());
    }

    @Test
    void erroresNoModificanNingunParticipante() throws Exception {
        var creeper = creeper();
        var player = player(20);
        String valido = objetivo(player.getId(), 1);
        explotar(creeper.getId(), valido + "," + objetivo(player.getId(), 2))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Objetivo repetido: " + player.getId()));
        explotar(creeper.getId(), objetivo(player.getId(), -1)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("distancia es obligatoria y no puede ser negativa"));
        explotar(player.getId(), valido).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El origen debe ser un Creeper"));
        explotar(creeper.getId(), objetivo(creeper.getId(), 1)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El objetivo debe ser Player o Zombie"));
        explotar(creeper.getId(), valido + "," + objetivo(Long.MAX_VALUE, 1)).andExpect(status().isNotFound());
        explotar(Long.MAX_VALUE, valido).andExpect(status().isNotFound());
        assertEquals(20, repository.findById(player.getId()).orElseThrow().getVida());
        var persistido = (Creeper) repository.findById(creeper.getId()).orElseThrow();
        assertEquals(20, persistido.getVida());
        assertFalse(persistido.isYaExploto());
    }

    @Test
    void rechazaMuertosYEntradaIncompleta() throws Exception {
        var creeper = creeper();
        var player = player(20);
        player.recibirDano(20);
        repository.save(player);
        explotar(creeper.getId(), objetivo(player.getId(), 1)).andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El objetivo esta muerto: " + player.getId()));
        explotar(creeper.getId(), "").andExpect(status().isBadRequest());
        explotar(creeper.getId(), "null").andExpect(status().isBadRequest());
        explotar(creeper.getId(), "{\"id\":" + player.getId() + "}").andExpect(status().isBadRequest());
        creeper.recibirDano(20);
        repository.save(creeper);
        explotar(creeper.getId(), objetivo(player.getId(), 1)).andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El creeper esta muerto"));
    }

    @Test
    void creeperTieneCrudCompleto() throws Exception {
        String url = mvc.perform(post("/entidades").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipo\":\"Creeper\",\"vida\":20,\"danoBase\":5,\"velocidad\":3}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.yaExploto").value(false))
                .andReturn().getResponse().getHeader("Location");
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
                .content("{\"vida\":30,\"danoBase\":6,\"velocidad\":4}"))
                .andExpect(status().isOk());
        mvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.vida").value(30));
        mvc.perform(get("/entidades")).andExpect(status().isOk());
        mvc.perform(delete(url)).andExpect(status().isNoContent());
        mvc.perform(get(url)).andExpect(status().isNotFound());
    }
}
