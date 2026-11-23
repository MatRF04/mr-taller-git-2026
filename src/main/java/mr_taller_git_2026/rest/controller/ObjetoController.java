package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mr_taller_git_2026.rest.dto.ObjetoResponse;
import mr_taller_git_2026.service.ObjetoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "Objetos externos")
@RestController
public class ObjetoController {

    private final ObjetoService service;

    public ObjetoController(ObjetoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar objetos de la API externa")
    @GetMapping(value = "/objetos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ObjetoResponse> listar() {
        return service.listar();
    }
}
