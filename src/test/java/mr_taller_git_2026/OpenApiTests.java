package mr_taller_git_2026;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void documentaLasOperacionesPublicas() throws Exception {
        var documento = mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
        for (String ruta : List.of("/", "/entidades", "/entidades/{id}", "/entidades/creepers/listos",
                "/entidades/zombie", "/entidades/esqueleto/disparar", "/entidades/comportamientos",
                "/sonidos", "/ui")) {
            documento.andExpect(jsonPath("$.paths['" + ruta + "'].get.summary").isString());
        }
        documento.andExpect(jsonPath("$.paths['/entidades'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/entidades/{id}'].put.summary").isString())
                .andExpect(jsonPath("$.paths['/entidades/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/entidades/{id}/explotar'].post.summary").isString());
    }

    @Test
    void documentaLasDosReglasYSusErrores() throws Exception {
        var documento = mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.info.description").value(org.hamcrest.Matchers.containsString("Regla 1")))
                .andExpect(jsonPath("$.info.description").value(org.hamcrest.Matchers.containsString("Regla 2")))
                .andExpect(jsonPath("$.paths['/entidades/{id}/explotar'].post.description").isString());
        for (String codigo : List.of("200", "400", "404", "409")) {
            documento.andExpect(jsonPath("$.paths['/entidades/{id}/explotar'].post.responses['" + codigo + "']").exists());
        }
        for (String codigo : List.of("400", "404", "409")) {
            documento.andExpect(jsonPath("$.paths['/entidades/{id}'].put.responses['" + codigo + "']").exists());
        }
        documento.andExpect(jsonPath("$.paths['/entidades'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/entidades/{id}'].get.responses['404']").exists())
                .andExpect(jsonPath("$.components.schemas.ErrorResponse.properties.error").exists());
    }

    @Test
    void documentaElEscenarioConLosRechazosDeLosCompaneros() throws Exception {
        var documento = mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/jugadas'].post.description").isString())
                .andExpect(jsonPath("$.paths['/jugadas'].get.summary").isString())
                .andExpect(jsonPath("$.paths['/jugadas/{id}'].get.responses['404']").exists());
        for (String codigo : List.of("201", "400", "404", "409", "422", "502")) {
            documento.andExpect(jsonPath("$.paths['/jugadas'].post.responses['" + codigo + "']").exists());
        }
    }

    @Test
    void publicaSwaggerUiConLaEspecificacionLocal() throws Exception {
        mvc.perform(get("/swagger-ui.html")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
        mvc.perform(get("/v3/api-docs/swagger-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("/v3/api-docs"));
    }
}
