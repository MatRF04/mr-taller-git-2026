package mr_taller_git_2026.service;

import mr_taller_git_2026.rest.dto.ObjetoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ObjetoService {

    private final WebClient cliente;

    public ObjetoService(WebClient.Builder builder) {
        this.cliente = builder.baseUrl("https://api.restful-api.dev").build();
    }

    public Flux<ObjetoResponse> listar() {
        return cliente.get()
                .uri("/objects")
                .retrieve()
                .bodyToFlux(ObjetoResponse.class);
    }
}
