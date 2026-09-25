package mr_taller_git_2026.entidades;

public class Esqueleto extends Entidad {
    private boolean usaArco;

    public Esqueleto() {
        super();
    }

    public Esqueleto(int vida, int danoBase, int velocidad, boolean usaArco) {
        super(vida, danoBase, velocidad);
        this.usaArco = usaArco;
    }

    @Override
    public String describirComportamiento() {
        return usaArco ? "Ataca al jugador con un arco." : "Ataca al jugador cuerpo a cuerpo.";
    }

    public boolean isUsaArco() {
        return usaArco;
    }
}
