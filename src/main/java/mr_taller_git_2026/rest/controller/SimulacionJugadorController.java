package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mr_taller_git_2026.rest.dto.ImpactoJugadorResponse;
import mr_taller_git_2026.service.SimulacionJugadorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Explosiones")
public class SimulacionJugadorController {
    private final SimulacionJugadorService service;

    public SimulacionJugadorController(SimulacionJugadorService service) {
        this.service = service;
    }

    @GetMapping("/simulaciones/creeper/jugador")
    @Operation(summary = "Simular una explosión en el origen sobre un jugador de A2",
            description = "Pide el jugador a A2 (GET /jugador), usa su salud y su posición y aplica la regla 2: "
                    + "a tres bloques o menos pierde hasta diez puntos de vida. No guarda nada.")
    public ImpactoJugadorResponse simular(@RequestParam(defaultValue = "20") int saludMaxima,
            @RequestParam(defaultValue = "0") double x, @RequestParam(defaultValue = "0") double y,
            @RequestParam(defaultValue = "0") double z) {
        return service.simular(saludMaxima, x, y, z);
    }
}
