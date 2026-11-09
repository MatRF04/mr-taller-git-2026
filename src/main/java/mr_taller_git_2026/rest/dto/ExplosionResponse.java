package mr_taller_git_2026.rest.dto;

import java.util.List;

public record ExplosionResponse(Long creeperId, int vidaCreeper, List<Afectado> afectados) {
    public record Afectado(Long id, String tipo, int danoRecibido, int vidaFinal) {}
}
