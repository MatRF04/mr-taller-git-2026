package mr_taller_git_2026.domain.exception;

/** La API de un compañero respondió con un error 4xx: su regla rechazó la jugada. */
public abstract class RechazoCompaneroException extends CompaneroException {
    private final int estadoRemoto;
    private final String detalleRemoto;

    protected RechazoCompaneroException(String companero, int estadoRemoto, String detalleRemoto,
            Throwable causa) {
        super(companero, "La jugada fue rechazada por " + companero, causa);
        this.estadoRemoto = estadoRemoto;
        this.detalleRemoto = detalleRemoto;
    }

    public int getEstadoRemoto() {
        return estadoRemoto;
    }

    public String getDetalleRemoto() {
        return detalleRemoto;
    }
}
