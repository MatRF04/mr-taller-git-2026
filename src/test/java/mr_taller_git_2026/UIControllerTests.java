package mr_taller_git_2026;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UIControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void devuelveLasCuatroCapacidadesDeAmbasFamilias() throws Exception {
        mvc.perform(get("/ui"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tipo").value("Cerdo"))
                .andExpect(jsonPath("$[0].imagen").value("cerdo"))
                .andExpect(jsonPath("$[0].posicion").value("0, 0, 0"))
                .andExpect(jsonPath("$[0].sonido").value("El cerdo gruñe: oinc."))
                .andExpect(jsonPath("$[0].velocidad").value(3))
                .andExpect(jsonPath("$[1].tipo").value("BloqueMusical"))
                .andExpect(jsonPath("$[1].imagen").value("bloque_musical"))
                .andExpect(jsonPath("$[1].posicion").value("1, 0, 0"))
                .andExpect(jsonPath("$[1].sonido").value("El bloque musical reproduce una nota."))
                .andExpect(jsonPath("$[1].velocidad").value(0));
    }
}
