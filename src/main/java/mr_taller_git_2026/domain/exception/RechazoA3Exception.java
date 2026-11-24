package mr_taller_git_2026.domain.exception;

/** La regla de A3 rechazó la jugada. */
public class RechazoA3Exception extends RechazoCompaneroException {
    public RechazoA3Exception(String companero, int estadoRemoto, String detalleRemoto, Throwable causa) {
        super(companero, estadoRemoto, detalleRemoto, causa);
    }
}
