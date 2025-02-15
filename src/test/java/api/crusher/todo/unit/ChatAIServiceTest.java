package api.crusher.todo.unit;

import api.crusher.todo.application.service.ChatAIService;
import api.crusher.todo.infrastructure.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ChatAIServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpecMock;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpecMock;

    @InjectMocks
    private ChatAIService chatAIService;

    @BeforeEach
    @DisplayName("ChatAIServiceTest 초기화")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        given(chatClient.prompt()).willReturn(requestSpecMock);
        given(requestSpecMock.user(anyString())).willReturn(requestSpecMock);
        given(requestSpecMock.call()).willReturn(callResponseSpecMock);
        given(callResponseSpecMock.content()).willReturn("""
                <think> think </think>
                normal answer
                ```json
                {
                    "title": "title",
                    "description": "description"
                }
                ```
                """);
    }


    @Nested
    @DisplayName("askToDeepSeekAI 메서드는")
    class ExtractJson {
        @Test
        @DisplayName("HTML 태그를 제외한 답변을 반환한다")
        void shouldExtractJsonFromInput() {
            // Given
            String input = "Some text before {\"key\":\"value\"} some text after";
            String expected = "{\"key\":\"value\"}";

            // When
            String result = JsonUtil.extractJson(input);

            // Then
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("JSON이 없는 경우 IllegalArgumentException을 던진다")
        void shouldThrowExceptionWhenJsonNotFound() {
            // Given
            String input = "No JSON here";

            // When / Then
            assertThrows(IllegalArgumentException.class, () -> JsonUtil.extractJson(input));
        }
    }

    @Nested
    @DisplayName("exceptTag 메서드는")
    class ExceptTag {
        @Test
        @DisplayName("입력에서 HTML 태그를 제외한 문자열을 반환한다")
        void shouldRemoveHtmlTagsFromInput() {
            // Given
            String input = "<p>This is a <b>test</b></p>";
            String expected = "This is a test";

            // When
            String result = JsonUtil.exceptTag(input);

            // Then
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("HTML 태그가 없는 경우 입력을 그대로 반환한다")
        void shouldReturnInputWhenNoHtmlTags() {
            // Given
            String input = "No HTML tags here";
            String expected = "No HTML tags here";

            // When
            String result = JsonUtil.exceptTag(input);

            // Then
            assertEquals(expected, result);
        }
    }
}