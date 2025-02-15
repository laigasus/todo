package api.crusher.todo.unit;

import api.crusher.todo.application.dto.TaskDTO;
import api.crusher.todo.application.service.TaskService;
import api.crusher.todo.config.TestDatabaseConfig;
import api.crusher.todo.fixture.FixtureFactory;
import api.crusher.todo.infrastructure.repository.TaskRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestDatabaseConfig.class)
public class TaskJpaTest {

    final FixtureMonkey fixtureMonkey = FixtureFactory.create();

    @Autowired
    TaskRepository taskRepository;

    TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Nested
    @DisplayName("Create 메서드는")
    class Create {
        @Test
        @DisplayName("정상적으로 저장한다")
        public void shouldStoreWithCorrectData() {
            // given
            var given = fixtureMonkey.giveMeBuilder(TaskDTO.class)
                    .sample();

            // when
            var actual = taskService.create(given);

            // then
            assertNotNull(actual);
        }
    }

    @Nested
    @DisplayName("Read 메서드는")
    class Read {
        List<TaskDTO> fixtures = fixtureMonkey.giveMeBuilder(TaskDTO.class)
                .sampleList(5);

        Pageable pageable = Pageable.unpaged();

        Long id;

        @BeforeEach
        @Sql("/data/clearAll.sql")
        void given() {
            // given
            id = taskService.createAll(fixtures).getFirst();
        }

        @Test
        @DisplayName("정상적으로 조회한다")
        void readTask() {
            // when
            var actual = taskService.findById(id);

            assertNotNull(actual);
        }

        @Test
        @DisplayName("모든 Task를 조회한다")
        void readTasks() {
            // when
            var expected = fixtures.size();

            var actual = taskService.findAll(pageable);

            assertEquals(expected, actual.getTotalElements());
        }
    }

    @Nested
    @DisplayName("Update 메서드는")
    class Update {
        TaskDTO before = fixtureMonkey.giveMeBuilder(TaskDTO.class)
                .sample();

        @BeforeEach
        void given() {
            before = before.toBuilder()
                    .id(taskService.create(before))
                    .build();
        }

        @Test
        @DisplayName("정상적으로 업데이트한다")
        public void updateTask() {
            // given
            var given = fixtureMonkey.giveMeBuilder(TaskDTO.class)
                    .set("id", before.getId())
                    .sample();

            // when
            var actual = taskService.update(given);

            // then
            assertNotNull(actual);
        }
    }

    @Nested
    @DisplayName("Delete 메서드는")
    class Delete {
        @BeforeEach
        @Sql("/data/clearAll.sql")
        void given() {
        }

        @Test
        @DisplayName("정상적으로 삭제한다")
        public void deleteTask() {
            // given
            var given = fixtureMonkey.giveMeBuilder(TaskDTO.class)
                    .sample();

            var id = taskService.create(given);

            // when
            taskService.delete(id);

            // then
            assertThrows(ResourceNotFoundException.class, () -> taskService.findById(id));
        }

    }
}
