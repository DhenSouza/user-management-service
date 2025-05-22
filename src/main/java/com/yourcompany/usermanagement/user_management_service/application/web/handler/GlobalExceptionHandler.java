package com.yourcompany.usermanagement.user_management_service.application.web.handler;

import com.yourcompany.usermanagement.user_management_service.Domain.exception.LoginException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
            = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               WebRequest request) {
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();

        List<Problem.Field> problemFields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

                    return Problem.Field.builder()
                            .name(fieldError.getField())
                            .userMessage(message)
                            .build();
                }).collect(Collectors.toList());

        Problem problem = createProblemBuilder(HttpStatus.BAD_REQUEST, problemType, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLoginException(LoginException ex, WebRequest request) {

        ProblemType problemType = ProblemType.CREDENCIAIS_INVALIDAS;
        String detail = ex.getMessage();

        List<Problem.Field> problemFields = Arrays.asList(
                Problem.Field.builder()
                        .name("email")
                        .userMessage("E-mail inválido ou não registrado.")
                        .build(),
                Problem.Field.builder()
                        .name("password")
                        .userMessage("Senha incorreta.")
                        .build()
        );

        Problem problem = createProblemBuilder(HttpStatus.UNAUTHORIZED, problemType, detail)
                .userMessage("Usuário ou senha inválidos. Por favor, verifique suas credenciais e tente novamente.")
                .fields(problemFields)
                .build();

        return new ResponseEntity<>(problem, HttpStatus.UNAUTHORIZED);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
                                                        ProblemType problemType, String detail) {
        return Problem.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}

