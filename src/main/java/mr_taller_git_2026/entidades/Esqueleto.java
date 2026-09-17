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

    public boolean isUsaArco() {
        return usaArco;
    }

    public void setUsaArco(boolean usaArco) {
        this.usaArco = usaArco;
    }
}
