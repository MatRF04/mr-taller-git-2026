package mr_taller_git_2026.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Cuerpo de todas las respuestas de error. Solo documenta el formato en el OpenAPI. */
@Schema(description = "Error con el mensaje de la regla o validación que falló")
public record ErrorResponse(@Schema(example = "El creeper ya explotó") String error) {
}
