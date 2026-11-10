package mr_taller_git_2026.rest.dto;

public record EntidadRequest(String tipo, Integer vida, Integer danoBase, Integer velocidad,
        Boolean hostilidad, Boolean usaArco, Boolean comercializacion, Boolean montable,
        Integer velocidadAumentada) {
    public EntidadRequest {
        hostilidad = Boolean.TRUE.equals(hostilidad);
        usaArco = Boolean.TRUE.equals(usaArco);
        comercializacion = Boolean.TRUE.equals(comercializacion);
        montable = Boolean.TRUE.equals(montable);
        velocidadAumentada = velocidadAumentada == null ? 0 : velocidadAumentada;
    }
}
