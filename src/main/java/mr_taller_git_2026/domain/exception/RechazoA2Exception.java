package mr_taller_git_2026.domain.exception;

/** La regla de A2 rechazó la jugada. */
public class RechazoA2Exception extends RechazoCompaneroException {
    public RechazoA2Exception(String companero, int estadoRemoto, String detalleRemoto, Throwable causa) {
        super(companero, estadoRemoto, detalleRemoto, causa);
    }
}
