package api.crusher.todo.integration;

import api.crusher.todo.config.BaseIntegration;
import api.crusher.todo.infrastructure.repository.TaskRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskResourceTest extends BaseIntegration {

    @Autowired
    public TaskRepository taskRepository;

    @Nested
    @DisplayName("getTasks")
    class GetTasksRequest {

        @Test
        @DisplayName("데이터베이스에 저장된 모든 태스크를 조회")
        @Sql("/data/taskData.sql")
        void success() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/tasks")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", Matchers.hasKey("_embedded"))
                    .body("", Matchers.hasKey("_links"))
                    .body("", Matchers.hasKey("page"))
                    .body("page.totalElements", Matchers.equalTo(2));
        }

        @Test
        @DisplayName("page, size 값이 범주 밖이어서 아무 값도 안가져올 때")
        void outOfRange() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/tasks?page=100&size=100")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(0));
        }
    }

    @Nested
    @DisplayName("getTask")
    class GetTask {

        @Test
        @DisplayName("특정 ID를 가진 태스크")
        @Sql("/data/taskData.sql")
        void success() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/tasks/1001")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("title", Matchers.equalTo("title 1"))
                    .body("_links.self.href", Matchers.endsWith("/api/tasks/1001"));
        }

        @Test
        @DisplayName("존재하지 않는 ID를 가진 태스크")
        void notFound() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .get("/api/tasks/1666")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("createTask")
    class CreateTask {

        @Test
        @DisplayName("새로운 태스크를 생성")
        void success() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/createTaskRequest.json"))
                    .when()
                    .post("/api/tasks")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            assertEquals(1, taskRepository.count());
        }

        @Test
        @DisplayName("일부 값이 없는 경우")
        void missingValue() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/createTaskRequest_missingValue.json"))
                    .when()
                    .post("/api/tasks")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
            assertEquals(0, taskRepository.count());
        }

    }

    @Nested
    @DisplayName("updateTask")
    class UpdateTask {

        @Test
        @DisplayName("기존 태스크 업데이트")
        @Sql("/data/taskData.sql")
        void success() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/createTaskRequest.json"))
                    .when()
                    .put("/api/tasks/1001")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/tasks/1001"));
            assertEquals("title 1", taskRepository.findById(1001L).orElseThrow().getTitle());
            assertEquals(2, taskRepository.count());
        }

        @Test
        @DisplayName("일부 값이 없는 경우")
        @Sql("/data/taskData.sql")
        void missingValue() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/createTaskRequest_missingValue.json"))
                    .when()
                    .put("/api/tasks/1001")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
            assertEquals("title 1", taskRepository.findById(1001L).orElseThrow().getTitle());
            assertEquals(2, taskRepository.count());
        }
    }

    @Nested
    @DisplayName("deleteTask")
    class DeleteTask {

        @Test
        @DisplayName("특정 ID를 가진 태스크")
        @Sql("/data/taskData.sql")
        void success() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .delete("/api/tasks/1001")
                    .then()
                    .statusCode(HttpStatus.OK.value());
            assertEquals(1, taskRepository.count());
        }

        @Test
        @DisplayName("존재하지 않는 ID를 가진 태스크")
        void notFound() {
            RestAssured
                    .given()
                    .accept(ContentType.JSON)
                    .when()
                    .delete("/api/tasks/1666")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
            assertEquals(0, taskRepository.count());
        }
    }
}