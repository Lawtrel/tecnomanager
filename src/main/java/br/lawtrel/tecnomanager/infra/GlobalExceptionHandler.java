package br.lawtrel.tecnomanager.infra;

import br.lawtrel.tecnomanager.exception.BusinessRuleException;
import br.lawtrel.tecnomanager.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Trata erro de "Recurso Não Encontrado" (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setType(URI.create("https://tecnomanager.com/erros/nao-encontrado"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // 2. Trata erro de Validação (ex: campo vazio) (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "Erro de validação";

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setTitle("Dados inválidos");
        problemDetail.setType(URI.create("https://tecnomanager.com/erros/dados-invalidos"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // 3. Trata qualquer outro erro inesperado (500)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor.");
        problemDetail.setTitle("Erro Interno");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        e.printStackTrace(); // Loga o erro no console para você ver
        return problemDetail;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Regra de Negócio Violada");
        problemDetail.setType(URI.create("https://tecnomanager.com/erros/regra-negocio"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}