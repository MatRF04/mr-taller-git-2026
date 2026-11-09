package mr_taller_git_2026.rest.controller;

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

@RestController
public class EntidadController {
    private final EntidadService service;

    public EntidadController(EntidadService service) {
        this.service = service;
    }

    @GetMapping("/entidades")
    public List<Entidad> listar(@RequestParam(defaultValue = "false") boolean vivas) {
        return vivas ? service.listarVivas() : service.listar();
    }

    @GetMapping("/entidades/creepers/listos")
    public List<Creeper> creepersListos() {
        return service.listarCreepersListos();
    }

    @GetMapping("/entidades/{id}")
    public Entidad obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping("/entidades")
    public ResponseEntity<Entidad> crear(
            @RequestBody EntidadRequest datos) {
        var entidad = service.crear(datos);
        return ResponseEntity.created(URI.create("/entidades/" + entidad.getId())).body(entidad);
    }

    @PutMapping("/entidades/{id}")
    public Entidad actualizar(
            @PathVariable Long id,
            @RequestBody EntidadRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/entidades/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/entidades/zombie")
    public Zombie crearZombie(
            @RequestParam int vida,
            @RequestParam int danoBase,
            @RequestParam int velocidad,
            @RequestParam boolean hostilidad) {
        return new Zombie(vida, danoBase, velocidad, hostilidad);
    }

    @GetMapping("/entidades/esqueleto/disparar")
    public Map<String, String> dispararEsqueleto(
            @RequestParam int vida,
            @RequestParam int danoBase,
            @RequestParam int velocidad,
            @RequestParam boolean usaArco,
            @RequestParam(required = false) Integer distancia) {
        Esqueleto esqueleto = new Esqueleto(vida, danoBase, velocidad, usaArco);
        String resultado = distancia == null
                ? esqueleto.disparar()
                : esqueleto.disparar(distancia);
        return Map.of("accion", resultado);
    }
}
