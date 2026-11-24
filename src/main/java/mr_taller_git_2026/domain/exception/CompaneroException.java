package mr_taller_git_2026.domain.exception;

/** Base de los errores que vienen de la API de un compañero (A2 o A3). */
public abstract class CompaneroException extends RuntimeException {
    private final String companero;

    protected CompaneroException(String companero, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.companero = companero;
    }

    /** Rol y nombre del compañero, por ejemplo "A2 (Santiago Baez)". */
    public String getCompanero() {
        return companero;
    }
}
