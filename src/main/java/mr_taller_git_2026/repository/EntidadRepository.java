package mr_taller_git_2026.repository;

import java.util.List;
import mr_taller_git_2026.domain.Creeper;
import mr_taller_git_2026.domain.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntidadRepository extends JpaRepository<Entidad, Long> {
    /** Entidades que siguen con vida (Spring Data deriva la consulta del nombre). */
    List<Entidad> findByVidaGreaterThan(int vida);

    /** Creepers vivos que todavia no explotaron: los que pueden iniciar una simulacion. */
    @Query("select c from Creeper c where c.yaExploto = false and c.vida > 0")
    List<Creeper> findCreepersListosParaExplotar();
}
