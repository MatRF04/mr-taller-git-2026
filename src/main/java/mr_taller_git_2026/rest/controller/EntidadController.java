package mr_taller_git_2026.rest.controller;

import java.util.Map;

import mr_taller_git_2026.domain.Esqueleto;
import mr_taller_git_2026.domain.Zombie;
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
