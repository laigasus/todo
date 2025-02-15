package api.crusher.todo.application.service;

import api.crusher.todo.application.TaskUseCase;
import api.crusher.todo.application.dto.TaskDTO;
import api.crusher.todo.domain.Task;
import api.crusher.todo.infrastructure.repository.TaskMapper;
import api.crusher.todo.infrastructure.repository.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class TaskService implements TaskUseCase {

    private final TaskRepository taskRepository;
    private static final TaskMapper taskMapper = TaskMapper.INSTANCE;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(final Pageable pageable) {
        Page<Task> page = taskRepository.findAll(pageable);
        return page.map(taskMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findById(@Positive final Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional
    public Long create(@Valid final TaskDTO taskDTO) {
        final Task task = taskMapper.toEntity(taskDTO);
        return taskRepository.save(task).getId();
    }

    @Override
    @Transactional
    public List<Long> createAll(@NotEmpty List<@Valid TaskDTO> taskDTOs) {
        List<Task> tasks = taskDTOs.stream().map(taskMapper::toEntity).toList();
        return taskRepository.saveAll(tasks).stream().map(Task::getId).toList();
    }

    @Override
    @Transactional
    public Long update(@Valid final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(taskDTO.getId())
                .orElseThrow(ResourceNotFoundException::new);

        return taskRepository.save(task.update(taskMapper.toEntity(taskDTO))).getId();
    }

    @Override
    @Transactional
    public void delete(@Positive final Long id) {
        taskRepository.deleteById(id);
    }

}
