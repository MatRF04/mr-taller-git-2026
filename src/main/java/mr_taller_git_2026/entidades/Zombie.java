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

    @Override
    public String describirComportamiento() {
        return hostilidad ? "Persigue y ataca al jugador." : "Deambula sin atacar al jugador.";
    }

    public boolean isHostilidad() {
        return hostilidad;
    }
}
