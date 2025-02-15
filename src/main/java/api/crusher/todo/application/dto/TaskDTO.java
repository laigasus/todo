package api.crusher.todo.application.dto;

import api.crusher.todo.domain.TaskState;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;


@Builder(toBuilder = true)
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskDTO {
    @Positive
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private TaskState state;

    @PastOrPresent
    private Date createdAt;

    @PastOrPresent
    private Date updatedAt;
}
