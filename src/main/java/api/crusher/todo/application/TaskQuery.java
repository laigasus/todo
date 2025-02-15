package api.crusher.todo.application;

import api.crusher.todo.application.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskQuery {
    Page<TaskDTO> findAll(Pageable pageable);

    TaskDTO findById(Long id);
}
