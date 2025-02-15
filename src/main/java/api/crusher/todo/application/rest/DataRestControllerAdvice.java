package api.crusher.todo.application.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class DataRestControllerAdvice extends ResponseEntityExceptionHandler {

    private final ResourceBundle bundle;

    @ExceptionHandler(NestedRuntimeException.class)
    public ProblemDetail handleNestedRuntimeException(
            NestedRuntimeException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create("about:blank"));
        problem.setTitle(ex.getClass().getSimpleName());
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errors", ex.getMostSpecificCause().getMessage());
        return problem;
    }

    @ExceptionHandler(RepositoryConstraintViolationException.class)
    public ProblemDetail handleRepositoryConstraintViolationException(
            RepositoryConstraintViolationException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create("about:blank"));
        problem.setTitle(bundle.getString("exception.violation.title"));
        problem.setDetail(bundle.getString("exception.violation.detail"));
        problem.setProperties(Map.of("errors", getErrors(ex)));
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create("about:blank"));
        problem.setTitle(bundle.getString("exception.violation.title"));
        problem.setDetail(bundle.getString("exception.violation.detail"));
        problem.setProperties(Map.of("errors", getErrors(ex)));
        problem.setInstance(URI.create(servletRequest.getRequestURI()));

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setType(URI.create("about:blank"));
        problem.setTitle(bundle.getString("exception.not_found.title"));
        problem.setDetail(bundle.getString("exception.not_found.detail"));
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }


    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        HttpServletRequest req = ((ServletWebRequest) request).getRequest();

        URI uri = UriComponentsBuilder.newInstance()
                .scheme(req.getScheme())
                .host(req.getServerName())
                .port(req.getServerPort())
                .path("/api")
                .build()
                .toUri();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setType(uri);
        problem.setTitle(bundle.getString("exception.no_resource.title"));
        problem.setDetail(bundle.getString("exception.no_resource.detail"));
        problem.setInstance(URI.create(req.getRequestURI()));

        return this.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private List<Map<String, Object>> getErrors(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(f -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("entity", f.getObjectName());
            map.put("property", f.getField());
            map.put("invalidValue", Optional.ofNullable(f.getRejectedValue()).orElse(""));
            map.put("message", f.getDefaultMessage());
            return Map.copyOf(map);
        }).toList();
    }

    private List<Map<String, Object>> getErrors(RepositoryConstraintViolationException ex) {
        return ex.getErrors().getFieldErrors().stream().map(f -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("entity", f.getObjectName());
            map.put("property", f.getField());
            map.put("invalidValue", Optional.ofNullable(f.getRejectedValue()).orElse(""));
            map.put("message", Optional.of(bundle.getString(f.getCode() + "." + f.getField())).orElse(""));
            return Map.copyOf(map);
        }).toList();
    }
}