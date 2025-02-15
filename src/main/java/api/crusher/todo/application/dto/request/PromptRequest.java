package api.crusher.todo.application.dto.request;


import jakarta.validation.constraints.NotEmpty;

public record PromptRequest(
        @NotEmpty(message = "값이 있어야 합니다.") String prompt
) {
}
