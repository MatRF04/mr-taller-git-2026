package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Estado")
@RestController
public class IndexController {

    @Operation(summary = "Consultar el estado de la API")
    @GetMapping("/")
    public String index() {
        return "API de Minecraft de Mateo. Servicio funcionando.";
    }
}
