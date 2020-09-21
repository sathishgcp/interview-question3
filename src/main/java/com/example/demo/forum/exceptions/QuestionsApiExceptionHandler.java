package com.example.demo.forum.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;

/**
 * ExceptionHandler to handle all {@link QuestionsApiException} thrown by all components.
 * @author  Sathish Pendem
 */
@ControllerAdvice
public class QuestionsApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(QuestionsApiExceptionHandler.class);

    /** ControlleAdvice to handle all {@link QuestionsApiException} throws by the components.
     * @param   ex  {@link QuestionsApiException} thrown by the api.
     * @return  ResponseEntity of type {@link QuestionsApiErrorResponse} to send to the user.
     */
    @ExceptionHandler(QuestionsApiException.class)
    public ResponseEntity<?> handleApiException(QuestionsApiException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                QuestionsApiErrorResponse.builder()
                        .errorCode(ex.getErrorCode())
                        .message(ex.getMessage())
                        .description(ofNullable(ex.getCause())
                                .map(cause -> cause.getLocalizedMessage())
                                .orElse(ex.getMessage())
                        )
                        .build(),
                ex.getStatus());
    }

    /** ControlleAdvice to handle all other unknown exceptions thrown by the api.
     * @param   ex  {@link Exception} thrown by the api.
     * @return  ResponseEntity of type {@link QuestionsApiErrorResponse} to send to the user with
     *          {@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                QuestionsApiErrorResponse.builder()
                        .errorCode("ERROR000")
                        .message(ex.getMessage())
                        .description("Unknown exception occurred while processing the request.")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
