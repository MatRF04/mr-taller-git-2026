package mr_taller_git_2026;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import mr_taller_git_2026.rest.controller.ObjetoController;
import mr_taller_git_2026.rest.dto.ObjetoResponse;
import mr_taller_git_2026.service.ObjetoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

@WebMvcTest(ObjetoController.class)
class ObjetoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ObjetoService service;

    @Test
    void devuelveLosObjetosDelServicioComoListaJson() throws Exception {
        when(service.listar()).thenReturn(Flux.just(
                new ObjetoResponse("1", "Teléfono", Map.of("color", "azul"))));

        var resultado = mvc.perform(get("/objetos")).andReturn();

        mvc.perform(asyncDispatch(resultado))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":"1","name":"Teléfono","data":{"color":"azul"}}]
                        """));
    }
}
