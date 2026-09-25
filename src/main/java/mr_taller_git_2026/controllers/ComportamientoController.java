package mr_taller_git_2026.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mr_taller_git_2026.entidades.Aldeano;
import mr_taller_git_2026.entidades.Entidad;
import mr_taller_git_2026.entidades.Zombie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComportamientoController {

    @GetMapping("/entidades/comportamientos")
    public List<Map<String, String>> comportamientos() {
        List<Entidad> entidades = List.of(
                new Zombie(20, 5, 3, true),
                new Aldeano(20, 0, 2, true));

        return entidades.stream()
                .map(entidad -> {
                    Map<String, String> respuesta = new LinkedHashMap<>();
                    respuesta.put("tipo", entidad.getClass().getSimpleName());
                    respuesta.put("comportamiento", entidad.describirComportamiento());
                    return respuesta;
                })
                .toList();
    }
}
