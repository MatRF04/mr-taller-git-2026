package mr_taller_git_2026.domain;

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

    public String disparar() {
        return usaArco ? "Dispara una flecha al jugador." : "No tiene arco: no puede disparar.";
    }

    public String disparar(int distancia) {
        validarNoNegativo(distancia, "distancia");
        if (!usaArco) {
            return "No tiene arco: no puede disparar a " + distancia + " bloques.";
        }
        return "Dispara una flecha al jugador a " + distancia + " bloques.";
    }

    public boolean isUsaArco() {
        return usaArco;
    }
}
