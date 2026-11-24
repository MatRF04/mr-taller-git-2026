package mr_taller_git_2026.service;

import mr_taller_git_2026.rest.dto.ImpactoJugadorResponse;
import mr_taller_git_2026.service.cliente.ClienteSantiago;
import org.springframework.stereotype.Service;

/** Aplica la regla 2 de la explosión sobre el jugador que construye A2, sin guardar nada. */
@Service
public class SimulacionJugadorService {
    private final ClienteSantiago a2;

    public SimulacionJugadorService(ClienteSantiago a2) {
        this.a2 = a2;
    }

    public ImpactoJugadorResponse simular(int saludMaxima, double x, double y, double z) {
        validarCoordenadas(x, y, z);
        if (saludMaxima <= 0) {
            throw new IllegalArgumentException("La salud máxima debe ser positiva");
        }
        var jugador = a2.obtenerJugador(null, saludMaxima, x, y, z);
        double distancia = distanciaAlOrigen(jugador.x(), jugador.y(), jugador.z());
        int vidaFinal = ExplosionService.vidaTrasExplosion(jugador.salud(), distancia);
        return new ImpactoJugadorResponse(jugador.id(), distancia, jugador.salud(),
                jugador.salud() - vidaFinal, vidaFinal);
    }

    static void validarCoordenadas(double x, double y, double z) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) {
            throw new IllegalArgumentException("Las coordenadas deben ser números finitos");
        }
    }

    /** El Creeper explota en el origen (0, 0, 0). */
    static double distanciaAlOrigen(double x, double y, double z) {
        double distancia = Math.hypot(Math.hypot(x, y), z);
        if (!Double.isFinite(distancia)) {
            throw new IllegalArgumentException("Distancia fuera de rango");
        }
        return distancia;
    }
}
