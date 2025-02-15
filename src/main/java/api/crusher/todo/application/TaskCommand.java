package api.crusher.todo.application;


import api.crusher.todo.application.dto.TaskDTO;

import java.util.List;

public interface TaskCommand {

    Long create(TaskDTO taskDTO);

    List<Long> createAll(List<TaskDTO> taskDTOs);

    Long update(TaskDTO taskDTO);

    void delete(Long id);
}
