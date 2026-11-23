package mr_taller_git_2026.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Explosión en cadena: el Creeper local alcanza al jugador de A2 y detona la TNT de A3")
public record JugadaRequest(
        @Schema(description = "Id del Creeper en esta API (vivo y sin explotar)", example = "1") Long creeperId,
        JugadorA2 jugadorA2,
        TntA3 tntA3) {

    @Schema(description = "Jugador que valida A2 con GET /jugador. El Creeper explota en el origen (0, 0, 0)")
    public record JugadorA2(
            @Schema(example = "Steve") String id,
            @Schema(example = "20") Integer saludMaxima,
            @Schema(example = "2") Double x,
            @Schema(example = "0") Double y,
            @Schema(example = "0") Double z) {}

    @Schema(description = "TNT de A3 y su objetivo; A3 simula la detonación con POST /api/entidades/{id}/explosiones")
    public record TntA3(
            @Schema(example = "tnt-1") String tntId,
            @Schema(example = "jugador1") String objetivoId) {}
}
