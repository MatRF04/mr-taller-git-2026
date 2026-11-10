package mr_taller_git_2026;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EntidadCrudTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void cicloCrudConPersistencia() throws Exception {
        String url = mvc.perform(post("/entidades").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"tipo":"Zombie","vida":20,"danoBase":5,"velocidad":3,"hostilidad":true}
                    """))
                .andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
        mvc.perform(get(url)).andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Zombie"))
                .andExpect(jsonPath("$.vida").value(20)).andExpect(jsonPath("$.hostilidad").value(true));
        mvc.perform(get("/entidades")).andExpect(status().isOk()).andExpect(jsonPath("$[*].id").isNotEmpty());
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"vida":30,"danoBase":6,"velocidad":4,"hostilidad":false}
                    """))
                .andExpect(status().isOk());
        mvc.perform(get(url)).andExpect(jsonPath("$.vida").value(30))
                .andExpect(jsonPath("$.hostilidad").value(false));
        mvc.perform(delete(url)).andExpect(status().isNoContent());
        mvc.perform(get(url)).andExpect(status().isNotFound());
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isNotFound());
        mvc.perform(delete(url)).andExpect(status().isNotFound());
    }

    @Test
    void rechazaDatosInvalidos() throws Exception {
        for (String datos : new String[]{"{}",
                "{\"tipo\":\"Player\",\"vida\":0,\"danoBase\":1,\"velocidad\":1}",
                "{\"tipo\":\"Player\",\"vida\":20,\"danoBase\":-1,\"velocidad\":1}",
                "{\"tipo\":\"Otro\",\"vida\":20,\"danoBase\":1,\"velocidad\":1}"}) {
            mvc.perform(post("/entidades").contentType(MediaType.APPLICATION_JSON).content(datos))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists());
        }
    }

    @Test
    void consultasDelRepositorioFiltranVivasYCreepersListos() throws Exception {
        String creeper = crear("{\"tipo\":\"Creeper\",\"vida\":20,\"danoBase\":5,\"velocidad\":3}");
        String player = crear("{\"tipo\":\"Player\",\"vida\":5,\"danoBase\":1,\"velocidad\":1}");
        Long creeperId = Long.valueOf(creeper.substring(creeper.lastIndexOf('/') + 1));
        Long playerId = Long.valueOf(player.substring(player.lastIndexOf('/') + 1));
        mvc.perform(get("/entidades/creepers/listos"))
                .andExpect(jsonPath("$[?(@.id == " + creeperId + ")].tipo").value("Creeper"));
        mvc.perform(post(creeper + "/explotar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"objetivos\":[{\"id\":" + playerId + ",\"distancia\":1}]}"))
                .andExpect(status().isOk());
        mvc.perform(get("/entidades/creepers/listos"))
                .andExpect(jsonPath("$[?(@.id == " + creeperId + ")]").isEmpty());
        mvc.perform(get("/entidades").param("vivas", "true"))
                .andExpect(jsonPath("$[?(@.id == " + playerId + ")]").isEmpty())
                .andExpect(jsonPath("$[?(@.id == " + creeperId + ")]").isEmpty());
        mvc.perform(get("/entidades"))
                .andExpect(jsonPath("$[?(@.id == " + playerId + ")].vida").value(0));
    }

    private String crear(String json) throws Exception {
        return mvc.perform(post("/entidades").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
    }
}
