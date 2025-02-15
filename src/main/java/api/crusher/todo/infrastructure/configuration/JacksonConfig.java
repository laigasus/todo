package api.crusher.todo.infrastructure.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;


@Configuration
public class JacksonConfig {
    @Bean
    @Description("Jackson ObjectMapper 커스텀")
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.featuresToDisable(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, // 알 수 없는 속성 무시
                DeserializationFeature.ACCEPT_FLOAT_AS_INT, // float를 int로 변환하는 것을 허용
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS // 날짜를 timestamp로 변환하는 것을 허용
        );
    }

}
