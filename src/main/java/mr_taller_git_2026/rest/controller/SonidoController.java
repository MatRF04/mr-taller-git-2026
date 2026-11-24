package mr_taller_git_2026.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;

import mr_taller_git_2026.domain.BloqueMusical;
import mr_taller_git_2026.domain.Cerdo;
import mr_taller_git_2026.domain.Sonoro;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sonidos")
@RestController
public class SonidoController {

    @Operation(summary = "Consultar qué sonido emite cada objeto")
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
