package mr_taller_git_2026.domain.exception;

/** La API del compañero no respondió a tiempo, falló (5xx) o devolvió una respuesta incompleta. */
public class CompaneroNoDisponibleException extends CompaneroException {
    public CompaneroNoDisponibleException(String companero, String mensaje, Throwable causa) {
        super(companero, mensaje, causa);
    }
}
