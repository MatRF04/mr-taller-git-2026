package mr_taller_git_2026;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ComportamientoControllerTests {

    private static final String URL =
            "/entidades/comportamientos?vida=%d&danoBase=5&velocidad=3&hostilidad=true&comercializacion=false";

    @Autowired
    private MockMvc mvc;

    @Test
    void devuelveElComportamientoDeCadaHija() throws Exception {
        mvc.perform(get(URL.formatted(20)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Zombie"))
                .andExpect(jsonPath("$[0].comportamiento").value("Persigue y ataca al jugador."))
                .andExpect(jsonPath("$[1].tipo").value("Aldeano"))
                .andExpect(jsonPath("$[1].comportamiento").value("No comercia con el jugador."));
    }

    @Test
    void vidaInvalidaDevuelve400() throws Exception {
        mvc.perform(get(URL.formatted(0)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
