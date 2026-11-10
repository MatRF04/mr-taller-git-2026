package mr_taller_git_2026.service;

import java.util.List;
import mr_taller_git_2026.domain.*;
import mr_taller_git_2026.domain.exception.EntidadNoEncontradaException;
import mr_taller_git_2026.repository.EntidadRepository;
import mr_taller_git_2026.rest.dto.EntidadRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EntidadService {
    private final EntidadRepository repository;

    public EntidadService(EntidadRepository repository) {
        this.repository = repository;
    }

    public List<Entidad> listar() {
        return repository.findAll();
    }

    public List<Entidad> listarVivas() {
        return repository.findByVidaGreaterThan(0);
    }

    public List<Creeper> listarCreepersListos() {
        return repository.findCreepersListosParaExplotar();
    }

    public Entidad obtener(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException(id));
    }

    @Transactional
    public Entidad crear(EntidadRequest datos) {
        validar(datos);
        if (datos.tipo() == null) {
            throw new IllegalArgumentException("tipo es obligatorio");
        }
        Entidad entidad = switch (datos.tipo()) {
            case "Creeper" -> new Creeper(datos.vida(), datos.danoBase(), datos.velocidad());
            case "Player" -> new Player(datos.vida(), datos.danoBase(), datos.velocidad());
            case "Zombie" -> new Zombie(datos.vida(), datos.danoBase(), datos.velocidad(), datos.hostilidad());
            case "Esqueleto" -> new Esqueleto(datos.vida(), datos.danoBase(), datos.velocidad(), datos.usaArco());
            case "Aldeano" -> new Aldeano(datos.vida(), datos.danoBase(), datos.velocidad(), datos.comercializacion());
            case "Cerdo" -> new Cerdo(datos.vida(), datos.danoBase(), datos.velocidad(), datos.hostilidad(), datos.montable());
            case "ZombiePequeno" -> new ZombiePequeño(datos.vida(), datos.danoBase(), datos.velocidad(), datos.hostilidad(), datos.velocidadAumentada());
            default -> throw new IllegalArgumentException("Tipo de entidad no soportado: " + datos.tipo());
        };
        return repository.save(entidad);
    }

    @Transactional
    public Entidad actualizar(Long id, EntidadRequest datos) {
        Entidad entidad = obtener(id);
        validar(datos);
        String tipoActual = entidad instanceof ZombiePequeño ? "ZombiePequeno" : entidad.getClass().getSimpleName();
        if (datos.tipo() != null && !datos.tipo().equals(tipoActual)) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de una entidad existente");
        }
        if (entidad instanceof Zombie zombie) zombie.setHostilidad(datos.hostilidad());
        if (entidad instanceof Esqueleto esqueleto) esqueleto.setUsaArco(datos.usaArco());
        if (entidad instanceof Aldeano aldeano) aldeano.setComercializacion(datos.comercializacion());
        if (entidad instanceof Cerdo cerdo) {
            cerdo.setHostilidad(datos.hostilidad());
            cerdo.setMontable(datos.montable());
        }
        if (entidad instanceof ZombiePequeño zombie) zombie.setVelocidadAumentada(datos.velocidadAumentada());
        entidad.actualizarAtributos(datos.vida(), datos.danoBase(), datos.velocidad());
        return repository.save(entidad);
    }

    @Transactional
    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private void validar(EntidadRequest datos) {
        if (datos.vida() == null || datos.danoBase() == null || datos.velocidad() == null) {
            throw new IllegalArgumentException("vida, danoBase y velocidad son obligatorios");
        }
        if (datos.velocidadAumentada() < 0) {
            throw new IllegalArgumentException("velocidadAumentada no puede ser negativa");
        }
        if (datos.vida() <= 0 || datos.danoBase() < 0 || datos.velocidad() < 0) {
            throw new IllegalArgumentException("La vida debe ser mayor que cero; danoBase y velocidad no pueden ser negativos");
        }
    }
}
