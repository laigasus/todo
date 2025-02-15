package api.crusher.todo.integration;

import api.crusher.todo.config.BaseIntegration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ChatAIControllerTest extends BaseIntegration {

    @Nested
    @DisplayName("AI에게 질문하기")
    class AskToAIRequest {

        @Test
        @DisplayName("정상 질문 후 제대로 된 응답")
        void shouldReturnProperResponseWhenAskedWithValidQuestion() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/ai/prompt?question=What%20is%20the%20weather%20today")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(Matchers.anything());
        }

        @Test
        @DisplayName("파라미터 없이 질문")
        void shouldReturnBadRequestWhenAskedWithoutParameter() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/ai/prompt")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("AI로부터 Task 생성하기")
    class CreateTaskFromAIRequest {
        @Test
        @DisplayName("정상 질문 후 제대로 된 생성")
        void shouldCreateTaskProperlyWhenAskedWithValidPrompt() {
            String requestBody = "{\"prompt\":\"Add a new task: pass the test. and todo is create application\"}";

            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(requestBody)
                    .get("/api/ai/task")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("title", Matchers.anything())
                    .body("description", Matchers.anything());
        }

        @Test
        @DisplayName("파라미터 없이 질문")
        void shouldReturnBadRequestWhenAskedWithoutPrompt() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/ai/task")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

}