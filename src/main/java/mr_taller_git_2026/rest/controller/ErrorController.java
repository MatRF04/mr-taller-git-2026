package mr_taller_git_2026.rest.controller;

import java.util.Map;
import mr_taller_git_2026.domain.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(TipoEntidadInvalidoException.class)
    public ResponseEntity<Map<String, String>> tipoInvalido(TipoEntidadInvalidoException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DistanciaInvalidaException.class)
    public ResponseEntity<Map<String, String>> distanciaInvalida(DistanciaInvalidaException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ObjetivoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> objetivoDuplicado(ObjetivoDuplicadoException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(EntidadMuertaException.class)
    public ResponseEntity<Map<String, String>> entidadMuerta(EntidadMuertaException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(CreeperYaExplotoException.class)
    public ResponseEntity<Map<String, String>> yaExploto(CreeperYaExplotoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> noEncontrada(EntidadNoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> valorInvalido(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
