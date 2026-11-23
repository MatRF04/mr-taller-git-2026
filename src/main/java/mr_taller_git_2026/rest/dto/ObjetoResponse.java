package mr_taller_git_2026.rest.dto;

import java.util.Map;

public record ObjetoResponse(String id, String name, Map<String, Object> data) {
}
