package mr_taller_git_2026.domain.exception;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(Long id) {
        super("No existe la entidad con id " + id);
    }
}
