package mr_taller_git_2026.entidades;

public abstract class Entidad {
    private int vida;
    private int danoBase;
    private int velocidad;

    protected Entidad() {
        this(1, 0, 0);
    }

    protected Entidad(int vida, int danoBase, int velocidad) {
        this.vida = validarVida(vida);
        this.danoBase = validarNoNegativo(danoBase, "danoBase");
        this.velocidad = validarNoNegativo(velocidad, "velocidad");
    }

    public abstract String describirComportamiento();

    protected static int validarVida(int vida) {
        if (vida <= 0) {
            throw new IllegalArgumentException("La vida debe ser mayor que cero");
        }
        return vida;
    }

    protected static int validarNoNegativo(int valor, String nombre) {
        if (valor < 0) {
            throw new IllegalArgumentException(nombre + " no puede ser negativo");
        }
        return valor;
    }

    public int getVida() {
        return vida;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public int getVelocidad() {
        return velocidad;
    }
}
