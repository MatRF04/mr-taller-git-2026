package mr_taller_git_2026.controllers;

import mr_taller_git_2026.entidades.Zombie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntidadController {

    @GetMapping("/entidades/zombie")
    public Zombie crearZombie(
            @RequestParam int vida,
            @RequestParam int danoBase,
            @RequestParam int velocidad,
            @RequestParam boolean hostilidad) {
        return new Zombie(vida, danoBase, velocidad, hostilidad);
    }
}
