package mr_taller_git_2026.entidades;

public class Aldeano extends Entidad {
    private boolean comercializacion;

    public Aldeano() {
        super();
    }

    public Aldeano(int vida, int danoBase, int velocidad, boolean comercializacion) {
        super(vida, danoBase, velocidad);
        this.comercializacion = comercializacion;
    }

    public boolean isComercializacion() {
        return comercializacion;
    }

    public void setComercializacion(boolean comercializacion) {
        this.comercializacion = comercializacion;
    }
}
