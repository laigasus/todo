package api.crusher.todo.infrastructure.repository;

import api.crusher.todo.application.dto.TaskDTO;
import api.crusher.todo.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDTO toDTO(Task task);

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDTO taskDTO);

}
 