package mr_taller_git_2026.domain.exception;

public abstract class ExplosionException extends RuntimeException {
    protected ExplosionException(String mensaje) {
        super(mensaje);
    }
}
