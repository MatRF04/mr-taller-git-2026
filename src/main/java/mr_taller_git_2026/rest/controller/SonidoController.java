package mr_taller_git_2026.rest.controller;

import java.util.List;
import java.util.Map;

import mr_taller_git_2026.domain.BloqueMusical;
import mr_taller_git_2026.domain.Cerdo;
import mr_taller_git_2026.domain.Sonoro;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SonidoController {

    @GetMapping("/sonidos")
    public List<Map<String, String>> sonidos() {
        List<Sonoro> objetos = List.of(new Cerdo(), new BloqueMusical());

        return objetos.stream()
                .map(objeto -> Map.of(
                        "tipo", objeto.getClass().getSimpleName(),
                        "sonido", objeto.emitirSonido()))
                .toList();
    }
}
