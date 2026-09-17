package mr_taller_git_2026.entidades;

public class ZombiePequeño extends Zombie {
    private int velocidadAumentada;

    public ZombiePequeño() {
        super();
    }

    public ZombiePequeño(int vida, int danoBase, int velocidad, boolean hostilidad,
            int velocidadAumentada) {
        super(vida, danoBase, velocidad, hostilidad);
        this.velocidadAumentada = velocidadAumentada;
    }

    public int getVelocidadAumentada() {
        return velocidadAumentada;
    }

    public void setVelocidadAumentada(int velocidadAumentada) {
        this.velocidadAumentada = velocidadAumentada;
    }
}
