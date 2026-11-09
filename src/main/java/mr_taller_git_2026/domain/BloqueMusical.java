package mr_taller_git_2026.domain;

public class BloqueMusical extends Bloque implements UICapable {
    private final int x;
    private final int y;
    private final int z;

    public BloqueMusical() {
        this(0, 0, 0);
    }

    public BloqueMusical(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getImagen() {
        return "bloque_musical";
    }

    @Override
    public String getPosicion() {
        return x + ", " + y + ", " + z;
    }

    @Override
    public int getVelocidad() {
        return 0;
    }

    @Override
    public String emitirSonido() {
        return "El bloque musical reproduce una nota.";
    }
}
