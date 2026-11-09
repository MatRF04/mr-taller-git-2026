package mr_taller_git_2026.domain;

/** Resultado de la explosion sobre un objetivo: dano efectivo y vida que le queda. */
public record Impacto(Entidad entidad, int danoRecibido, int vidaFinal) {
}
