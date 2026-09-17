package mr_taller_git_2026.entidades;

public abstract class Entidad {
    private int vida;
    private int danoBase;
    private int velocidad;

    protected Entidad() {
    }

    protected Entidad(int vida, int danoBase, int velocidad) {
        this.vida = vida;
        this.danoBase = danoBase;
        this.velocidad = velocidad;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public void setDanoBase(int danoBase) {
        this.danoBase = danoBase;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
}
