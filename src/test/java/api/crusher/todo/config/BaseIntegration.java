package api.crusher.todo.config;

import api.crusher.todo.TodoApplication;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.ollama.OllamaContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * 통합테스트마다 확장되어야 하는 추상 기본 클래스입니다.
 * Testcontainers Docker 인스턴스에 연결된 데이터 소스를 사용하여 Spring Boot 컨텍스트를 시작합니다.
 * 인스턴스는 모든 테스트에 재사용되며, 각 테스트 전에 모든 데이터가 삭제됩니다.
 */
@SpringBootTest(
        classes = TodoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@Sql("/data/clearAll.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIntegration {

    @ServiceConnection
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");
    protected static final OllamaContainer ollamaContainer = new OllamaContainer("ollama/ollama:latest");

    static {
        ollamaContainer.setPortBindings(List.of("11434:11434"));
        ollamaContainer.withReuse(true)
                .withExposedPorts(11434)
                .start();
        try {
            ollamaContainer.execInContainer("ollama", "pull", "deepseek-r1:1.5b");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        mySQLContainer.withUrlParam("serverTimezone", "UTC")
                .withDatabaseName("todo")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true)
                .start();
    }

/*    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }*/

    @LocalServerPort
    public int serverPort;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public String readResource(final String resourceName) {
        try {
            return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
