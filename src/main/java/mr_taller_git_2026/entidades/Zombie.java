package mr_taller_git_2026.entidades;

public class Zombie extends Entidad {
    private boolean hostilidad;

    public Zombie() {
        super();
    }

    public Zombie(int vida, int danoBase, int velocidad, boolean hostilidad) {
        super(vida, danoBase, velocidad);
        this.hostilidad = hostilidad;
    }

    public boolean isHostilidad() {
        return hostilidad;
    }

    public void setHostilidad(boolean hostilidad) {
        this.hostilidad = hostilidad;
    }
}
