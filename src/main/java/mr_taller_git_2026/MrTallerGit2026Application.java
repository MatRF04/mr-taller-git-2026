package mr_taller_git_2026;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "API de Minecraft", version = "1.0", description = """
        CRUD de entidades de Minecraft persistidas en H2 y simulación de explosiones de creepers.

        **Regla 1:** un Creeper vivo explota una sola vez y muere al hacerlo (queda con vida 0 y \
        `yaExploto = true`). Si ya explotó o está muerto responde 409.

        **Regla 2:** la explosión quita 10 de vida a cada Player o Zombie a 3 bloques o menos, \
        sin bajar su vida de 0. Fuera del radio no hay daño.

        Errores: 400 si el pedido no cumple una regla (tipo de entidad, distancia ausente o \
        negativa, objetivo repetido, datos inválidos); 404 si el id no existe; 409 si el estado \
        actual lo impide (Creeper que ya explotó, entidad muerta). El cuerpo siempre es \
        `{"error": "mensaje"}`."""))
@SpringBootApplication
public class MrTallerGit2026Application {

	public static void main(String[] args) {
		SpringApplication.run(MrTallerGit2026Application.class, args);
	}

}
