package mr_taller_git_2026.rest.controller;

import java.util.List;
import java.util.Map;

import mr_taller_git_2026.domain.Aldeano;
import mr_taller_git_2026.domain.Entidad;
import mr_taller_git_2026.domain.Zombie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComportamientoController {

    @GetMapping("/entidades/comportamientos")
    public List<Map<String, Object>> comportamientos(
            @RequestParam int vida,
            @RequestParam int danoBase,
            @RequestParam int velocidad,
            @RequestParam boolean hostilidad,
            @RequestParam boolean comercializacion) {
        List<Entidad> entidades = List.of(
                new Zombie(vida, danoBase, velocidad, hostilidad),
                new Aldeano(vida, danoBase, velocidad, comercializacion));

        return entidades.stream()
                .map(entidad -> Map.<String, Object>of(
                        "tipo", entidad.getClass().getSimpleName(),
                        "vida", entidad.getVida(),
                        "comportamiento", entidad.describirComportamiento()))
                .toList();
    }
}
