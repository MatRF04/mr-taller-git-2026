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

    @Override
    public String describirComportamiento() {
        return comercializacion ? "Comercia con el jugador." : "No comercia con el jugador.";
    }

    public boolean isComercializacion() {
        return comercializacion;
    }

    public void setComercializacion(boolean comercializacion) {
        this.comercializacion = comercializacion;
    }
}
