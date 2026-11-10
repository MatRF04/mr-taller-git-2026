package mr_taller_git_2026.domain;

@jakarta.persistence.Entity
public class Cerdo extends Entidad implements UICapable {
    private boolean hostilidad;
    private boolean montable;
    private final int x;
    private final int y;
    private final int z;

    public Cerdo() {
        this(1, 0, 0, false, false);
    }

    public Cerdo(int vida, int danoBase, int velocidad, boolean hostilidad, boolean montable) {
        this(vida, danoBase, velocidad, hostilidad, montable, 0, 0, 0);
    }

    public Cerdo(int vida, int danoBase, int velocidad, boolean hostilidad, boolean montable,
            int x, int y, int z) {
        super(vida, danoBase, velocidad);
        this.hostilidad = hostilidad;
        this.montable = montable;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getImagen() {
        return "cerdo";
    }

    @Override
    public String getPosicion() {
        return x + ", " + y + ", " + z;
    }

    @Override
    public String describirComportamiento() {
        return montable ? "Puede ser montado." : "No puede ser montado.";
    }

    @Override
    public String emitirSonido() {
        return "El cerdo gruñe: oinc.";
    }

    public boolean isHostilidad() {
        return hostilidad;
    }

    public boolean isMontable() {
        return montable;
    }

    public void setHostilidad(boolean hostilidad) {
        this.hostilidad = hostilidad;
    }

    public void setMontable(boolean montable) {
        this.montable = montable;
    }
}
