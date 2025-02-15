package api.crusher.todo.infrastructure.configuration;

import api.crusher.todo.application.projections.TaskCompactProjection;
import api.crusher.todo.application.validators.CreateTaskValidator;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@RequiredArgsConstructor
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    @Description("data rest 의 기본 경로를 /api 로 변경")
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration restConfig, CorsRegistry cors) {
        restConfig.setBasePath("/api");
        restConfig.getProjectionConfiguration().addProjection(TaskCompactProjection.class);
    }

    @Override
    @Description("data rest 에서 유효성 검사를 사용하고 싶을 때 활성화")
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener v) {
        v.addValidator("beforeCreate", new CreateTaskValidator());
        v.addValidator("beforeSave", new CreateTaskValidator());
    }
}