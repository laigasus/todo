package api.crusher.todo.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class YamlMessageSourceSupport extends AbstractMessageSource {

    private final ResourceBundle resourceBundle;

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        if (resourceBundle.containsKey(code)) {
            return new MessageFormat(resourceBundle.getString(code), locale);
        }
        return null;
    }

}