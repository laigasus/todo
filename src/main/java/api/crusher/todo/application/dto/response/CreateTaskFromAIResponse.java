package api.crusher.todo.application.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class CreateTaskFromAIResponse {
    private String title;
    private String description;
}