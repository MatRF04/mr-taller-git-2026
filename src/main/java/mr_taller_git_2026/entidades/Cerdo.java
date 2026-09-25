package mr_taller_git_2026.entidades;

public class Cerdo extends Entidad {
    private boolean hostilidad;
    private boolean montable;

    public Cerdo() {
        super();
    }

    public Cerdo(int vida, int danoBase, int velocidad, boolean hostilidad, boolean montable) {
        super(vida, danoBase, velocidad);
        this.hostilidad = hostilidad;
        this.montable = montable;
    }

    @Override
    public String describirComportamiento() {
        return montable ? "Puede ser montado." : "No puede ser montado.";
    }

    public boolean isHostilidad() {
        return hostilidad;
    }

    public void setHostilidad(boolean hostilidad) {
        this.hostilidad = hostilidad;
    }

    public boolean isMontable() {
        return montable;
    }

    public void setMontable(boolean montable) {
        this.montable = montable;
    }
}
