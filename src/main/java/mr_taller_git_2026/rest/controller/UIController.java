package mr_taller_git_2026.rest.controller;

import java.util.List;
import java.util.Map;

import mr_taller_git_2026.domain.BloqueMusical;
import mr_taller_git_2026.domain.Cerdo;
import mr_taller_git_2026.domain.UICapable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UIController {

    @GetMapping("/ui")
    public List<Map<String, Object>> ui() {
        List<UICapable> objetos = List.of(
                new Cerdo(10, 0, 3, false, true, 0, 0, 0),
                new BloqueMusical(1, 0, 0));

        return objetos.stream()
                .map(objeto -> Map.<String, Object>of(
                        "tipo", objeto.getClass().getSimpleName(),
                        "imagen", objeto.getImagen(),
                        "posicion", objeto.getPosicion(),
                        "sonido", objeto.emitirSonido(),
                        "velocidad", objeto.getVelocidad()))
                .toList();
    }
}
