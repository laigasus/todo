package api.crusher.todo.application.rest;

import api.crusher.todo.application.dto.request.PromptRequest;
import api.crusher.todo.application.dto.response.CreateTaskFromAIResponse;
import api.crusher.todo.application.service.ChatAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Chat AI와 상호작용하는 컨트롤러입니다.
 *
 * @author okjaeook
 * @since 20250213
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class ChatAIController {
    private final ChatAIService chatAIService;

    /**
     * askToAI
     *
     * @param question AI에게 보낼 질문
     * @return AI의 응답
     * @apiNote AI에게 질문을 보내고 응답을 받습니다.
     */
    @GetMapping("/prompt")
    public ResponseEntity<String> askToAI(@RequestParam(value = "question") String question) {
        return ResponseEntity.ok(chatAIService.askToDeepSeekAI(question));
    }

    /**
     * createTaskToAI
     *
     * @param request 프롬프트 요청
     * @return Task 생성 결과를 포함한 응답 엔티티
     * @apiNote 제공된 프롬프트를 사용하여 AI로 생성된 Task 를 제시합니다.
     */
    @GetMapping("/task")
    public ResponseEntity<CreateTaskFromAIResponse> createTaskFromAI(@Valid @RequestBody PromptRequest request) {
        return ResponseEntity.ok(chatAIService.addTaskWithDeepSeekAI(request.prompt()));
    }
}