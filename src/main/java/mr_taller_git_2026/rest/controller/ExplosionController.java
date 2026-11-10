package mr_taller_git_2026.rest.controller;

import mr_taller_git_2026.rest.dto.ExplosionRequest;
import mr_taller_git_2026.rest.dto.ExplosionResponse;
import mr_taller_git_2026.service.ExplosionService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExplosionController {
    private final ExplosionService service;

    public ExplosionController(ExplosionService service) { this.service = service; }

    @PostMapping("/entidades/{id}/explotar")
    public ExplosionResponse explotar(@PathVariable Long id, @RequestBody ExplosionRequest datos) {
        return service.explotar(id, datos);
    }
}
