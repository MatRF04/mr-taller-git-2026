package mr_taller_git_2026.domain;

@jakarta.persistence.Entity
public class Creeper extends Entidad {
    private boolean yaExploto;

    public Creeper() {
        super();
    }

    public Creeper(int vida, int danoBase, int velocidad) {
        super(vida, danoBase, velocidad);
    }

    public boolean isYaExploto() {
        return yaExploto;
    }

    @Override
    public String describirComportamiento() {
        return yaExploto ? "Ya exploto." : "Explota cerca de otras entidades.";
    }
}
