package mr_taller_git_2026.domain.exception;

public class JugadaNoEncontradaException extends RuntimeException {
    public JugadaNoEncontradaException(Long id) {
        super("No existe la jugada con id " + id);
    }
}
