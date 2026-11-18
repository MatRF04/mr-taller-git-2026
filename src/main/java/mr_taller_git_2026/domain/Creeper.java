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

    public void registrarExplosion() {
        yaExploto = true;
        registrarVida(0);
    }

    @Override
    public String describirComportamiento() {
        return yaExploto ? "Ya explotó." : "Explota cerca de otras entidades.";
    }
}
