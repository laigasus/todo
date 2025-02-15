package api.crusher.todo.application.projections;

import api.crusher.todo.domain.Task;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@SuppressWarnings("unused")
@Projection(name = "taskCompact", types = {Task.class})
public interface TaskCompactProjection {

    String getTitle();

    String getState();
}
