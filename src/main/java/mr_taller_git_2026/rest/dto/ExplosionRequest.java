package mr_taller_git_2026.rest.dto;

import java.util.List;

public record ExplosionRequest(List<Objetivo> objetivos) {
    public record Objetivo(Long id, Integer distancia) {}
}
