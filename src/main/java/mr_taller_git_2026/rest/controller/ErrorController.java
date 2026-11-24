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

    @ExceptionHandler(JugadaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> jugadaNoEncontrada(JugadaNoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(RechazoA2Exception.class)
    public ResponseEntity<Map<String, Object>> rechazoA2(RechazoA2Exception e) {
        return rechazo(e);
    }

    @ExceptionHandler(RechazoA3Exception.class)
    public ResponseEntity<Map<String, Object>> rechazoA3(RechazoA3Exception e) {
        return rechazo(e);
    }

    @ExceptionHandler(CompaneroNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> companeroNoDisponible(CompaneroNoDisponibleException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("error", e.getMessage(), "servicio", e.getCompanero()));
    }

    /** 422: el pedido llegó bien, pero la regla de un compañero no lo permite. */
    private static ResponseEntity<Map<String, Object>> rechazo(RechazoCompaneroException e) {
        return ResponseEntity.status(422).body(Map.of(
                "error", e.getMessage(),
                "rechazadoPor", e.getCompanero(),
                "estadoRemoto", e.getEstadoRemoto(),
                "detalleRemoto", e.getDetalleRemoto()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> valorInvalido(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
