package mr_taller_git_2026.entidades;

public class Player extends Entidad {
    public Player() {
        super();
    }

    public Player(int vida, int danoBase, int velocidad) {
        super(vida, danoBase, velocidad);
    }

    @Override
    public String describirComportamiento() {
        return "Explora el mundo y combate a las entidades.";
    }
}
