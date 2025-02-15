package api.crusher.todo.infrastructure.configuration;

import dev.akkinoc.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class ResourceBundleConfig {

    @Value("${spring.messages.basename:messages/exception}")
    private String basename;

    @Bean
    @Primary
    public MessageSource messageSource() {
        return new YamlMessageSourceSupport(resourceBundle());
    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle(basename, Locale.getDefault(), YamlResourceBundle.Control.INSTANCE);
    }
}