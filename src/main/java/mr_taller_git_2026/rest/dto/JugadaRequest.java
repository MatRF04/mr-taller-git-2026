package mr_taller_git_2026.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Explosión en cadena: el Creeper local explota en el origen, alcanza al jugador de A2 "
        + "y detona una TNT que daña a la entidad de A3")
public record JugadaRequest(
        @Schema(description = "Id del Creeper en esta API (vivo y sin explotar)", example = "1") Long creeperId,
        JugadorA2 jugadorA2,
        EntidadA3 entidadA3) {

    @Schema(description = "Explosión que A2 aplica con su regla de daño (POST /simulacion/explosiones): "
            + "el creeper y el jugador tienen que existir en la base de A2")
    public record JugadorA2(
            @Schema(description = "Id del jugador en A2", example = "steve-m") String id,
            @Schema(description = "Id del creeper de A2 que detona en cadena", example = "creeper-m") String creeperId,
            @Schema(description = "Radio de la explosión (mayor que 0)", example = "5") Double radio) {}

    @Schema(description = "Entidad de A3 alcanzada por la TNT, que detona en el origen. "
            + "A3 aplica su regla con POST /api/entidades/{id}/explosiones")
    public record EntidadA3(@Schema(example = "jugador1") String id) {}
}
