package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mr_taller_git_2026.rest.dto.ErrorResponse;
import mr_taller_git_2026.rest.dto.ExplosionRequest;
import mr_taller_git_2026.rest.dto.ExplosionResponse;
import mr_taller_git_2026.service.ExplosionService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Explosiones")
@RestController
public class ExplosionController {
    private final ExplosionService service;

    public ExplosionController(ExplosionService service) { this.service = service; }

    @Operation(summary = "Hacer explotar un creeper y calcular el daño a los objetivos",
            description = """
                    El Creeper `{id}` explota una sola vez y queda con vida 0 (regla 1). Cada objetivo \
                    Player o Zombie a 3 bloques o menos pierde 10 de vida, sin bajar de 0 (regla 2). \
                    La distancia la informa el cliente. Se validan todos los objetivos antes de \
                    cambiar el estado: si algo falla, la base queda igual. Los participantes se \
                    crean antes con `POST /entidades`.""")
    @ApiResponse(responseCode = "200", description = "Explosión aplicada y persistida", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "El origen no es un Creeper, un objetivo no es Player o Zombie, "
            + "la distancia falta o es negativa, o un objetivo está repetido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "El Creeper o un objetivo no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "El Creeper ya explotó, o el Creeper o un objetivo ya tiene vida 0",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/entidades/{id}/explotar")
    public ExplosionResponse explotar(@PathVariable Long id, @RequestBody ExplosionRequest datos) {
        return service.explotar(id, datos);
    }
}
