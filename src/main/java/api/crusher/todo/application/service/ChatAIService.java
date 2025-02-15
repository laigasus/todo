package api.crusher.todo.application.service;

import api.crusher.todo.application.dto.response.CreateTaskFromAIResponse;
import api.crusher.todo.infrastructure.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatAIService {
    private final ChatClient chatClient;

    public String askToDeepSeekAI(String question) {
        String content = think(question, "normal");

        return JsonUtil.exceptTag(content);
    }

    public CreateTaskFromAIResponse addTaskWithDeepSeekAI(String question) {
        BeanOutputConverter<CreateTaskFromAIResponse> parser = new BeanOutputConverter<>(CreateTaskFromAIResponse.class);

        String response = think(question, parser.getFormat());

        response = JsonUtil.extractJson(response);

        try {
            return parser.convert(Objects.requireNonNull(response));
        } catch (JsonParseException e) {
            return CreateTaskFromAIResponse.builder()
                    .title("정상적으로 처리되지 않았습니다")
                    .description("다시 시도해주세요")
                    .build();
        }
    }

    protected String think(String question, String format) {
        return Optional.ofNullable(chatClient.prompt(question)
                .user(format)
                .call()
                .content()).orElse("");
    }
}