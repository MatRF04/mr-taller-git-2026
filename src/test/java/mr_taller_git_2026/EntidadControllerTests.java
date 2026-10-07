package mr_taller_git_2026;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EntidadControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void vidaInvalidaDevuelve400() throws Exception {
        mvc.perform(get("/entidades/zombie?vida=0&danoBase=5&velocidad=3&hostilidad=true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void dispararSinDistancia() throws Exception {
        mvc.perform(get("/entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accion").value("Dispara una flecha al jugador."));
    }

    @Test
    void dispararConDistanciaNegativaDevuelve400() throws Exception {
        mvc.perform(get("/entidades/esqueleto/disparar?vida=20&danoBase=3&velocidad=2&usaArco=true&distancia=-4"))
                .andExpect(status().isBadRequest());
    }
}
