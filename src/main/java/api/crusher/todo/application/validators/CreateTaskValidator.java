package api.crusher.todo.application.validators;

import api.crusher.todo.domain.Task;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component("beforeCreateTaskValidator")
@RequiredArgsConstructor
public class CreateTaskValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required");
    }
}
