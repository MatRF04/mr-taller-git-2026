package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import mr_taller_git_2026.domain.Jugada;
import mr_taller_git_2026.rest.dto.ErrorResponse;
import mr_taller_git_2026.rest.dto.JugadaRequest;
import mr_taller_git_2026.service.EscenarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Escenario POO-09")
@RestController
public class JugadaController {
    private final EscenarioService service;

    public JugadaController(EscenarioService service) {
        this.service = service;
    }

    @Operation(summary = "Explosión en cadena validada por A2 y A3", description = """
            El Creeper local explota y detona en cadena el creeper de A2 (Santiago Baez), cuya regla de \
            daño (decae con la distancia, POST /simulacion/explosiones) se aplica a su jugador, y una TNT \
            cuya regla aplica A3 (Lucas Fadul) con POST /api/entidades/{id}/explosiones. Primero se \
            aplican las reglas locales del Creeper, después A2 y por último A3. Solo si las dos aceptan, \
            el Creeper queda explotado y la jugada se guarda en H2. Si alguna rechaza, la base local \
            queda igual y la respuesta indica quién rechazó.""")
    @ApiResponse(responseCode = "201", description = "Jugada aceptada por A2 y A3 y guardada", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Faltan datos o el origen no es un Creeper",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "El Creeper no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "El Creeper ya explotó o está muerto",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "422", description = "La regla de A2 o de A3 rechazó la jugada; "
            + "rechazadoPor dice quién y detalleRemoto trae su respuesta")
    @ApiResponse(responseCode = "502", description = "A2 o A3 no respondió; servicio dice cuál")
    @PostMapping("/jugadas")
    public ResponseEntity<Jugada> jugar(@RequestBody JugadaRequest datos) {
        Jugada jugada = service.jugar(datos);
        return ResponseEntity.created(URI.create("/jugadas/" + jugada.getId())).body(jugada);
    }

    @Operation(summary = "Listar las jugadas guardadas")
    @GetMapping("/jugadas")
    public List<Jugada> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener una jugada guardada")
    @ApiResponse(responseCode = "200", description = "Jugada encontrada", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "No existe una jugada con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/jugadas/{id}")
    public Jugada obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}
