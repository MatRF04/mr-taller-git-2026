package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import mr_taller_git_2026.rest.dto.ErrorResponse;
import java.util.Map;
import mr_taller_git_2026.service.EntidadService;
import mr_taller_git_2026.domain.Entidad;
import mr_taller_git_2026.rest.dto.EntidadRequest;
import java.util.List;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import mr_taller_git_2026.domain.Creeper;
import mr_taller_git_2026.domain.Esqueleto;
import mr_taller_git_2026.domain.Zombie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Entidades")
@RestController
public class EntidadController {
    private final EntidadService service;

    public EntidadController(EntidadService service) {
        this.service = service;
    }

    @Operation(summary = "Listar entidades")
    @GetMapping("/entidades")
    public List<Entidad> listar(@RequestParam(defaultValue = "false") boolean vivas) {
        return vivas ? service.listarVivas() : service.listar();
    }

    @Operation(summary = "Listar creepers listos para explotar")
    @GetMapping("/entidades/creepers/listos")
    public List<Creeper> creepersListos() {
        return service.listarCreepersListos();
    }

    @Operation(summary = "Obtener una entidad por ID")
    @ApiResponse(responseCode = "200", description = "Entidad encontrada", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "No existe una entidad con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/entidades/{id}")
    public Entidad obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @Operation(summary = "Crear una entidad", description = """
            `tipo` es obligatorio: Creeper, Player, Zombie, Esqueleto, Aldeano, Cerdo o ZombiePequeno. \
            `vida` debe ser mayor que 0 y `danoBase`, `velocidad` y `velocidadAumentada` no negativos.""")
    @ApiResponse(responseCode = "201", description = "Entidad creada", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Tipo desconocido o atributos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/entidades")
    public ResponseEntity<Entidad> crear(
            @RequestBody EntidadRequest datos) {
        var entidad = service.crear(datos);
        return ResponseEntity.created(URI.create("/entidades/" + entidad.getId())).body(entidad);
    }

    @Operation(summary = "Actualizar una entidad", description = """
            Reemplaza vida, daño base y velocidad. No cambia el tipo ni reinicia `yaExploto`. \
            Una entidad muerta o un Creeper que ya explotó no se puede actualizar.""")
    @ApiResponse(responseCode = "200", description = "Entidad actualizada", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Atributos inválidos o cambio de tipo",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "No existe una entidad con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "La entidad está muerta o el Creeper ya explotó",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/entidades/{id}")
    public Entidad actualizar(
            @PathVariable Long id,
            @RequestBody EntidadRequest datos) {
        return service.actualizar(id, datos);
    }

    @Operation(summary = "Eliminar una entidad")
    @ApiResponse(responseCode = "204", description = "Entidad eliminada")
    @ApiResponse(responseCode = "404", description = "No existe una entidad con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/entidades/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Simular un zombie con los atributos indicados")
    @GetMapping("/entidades/zombie")
    public Zombie crearZombie(
            @RequestParam int vida,
            @RequestParam int danoBase,
            @RequestParam int velocidad,
            @RequestParam boolean hostilidad) {
        EntidadService.validarAtributos(vida, danoBase, velocidad);
        return new Zombie(vida, danoBase, velocidad, hostilidad);
    }

    @Operation(summary = "Simular el disparo de un esqueleto")
    @GetMapping("/entidades/esqueleto/disparar")
    public Map<String, String> dispararEsqueleto(
            @RequestParam int vida,
            @RequestParam int danoBase,
            @RequestParam int velocidad,
            @RequestParam boolean usaArco,
            @RequestParam(required = false) Integer distancia) {
        EntidadService.validarAtributos(vida, danoBase, velocidad);
        EntidadService.validarDistanciaDisparo(distancia);
        Esqueleto esqueleto = new Esqueleto(vida, danoBase, velocidad, usaArco);
        String resultado = distancia == null
                ? esqueleto.disparar()
                : esqueleto.disparar(distancia);
        return Map.of("accion", resultado);
    }
}
