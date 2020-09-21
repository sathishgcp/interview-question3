package com.example.demo.forum.exceptions;

import lombok.Builder;
import lombok.Getter;

/**
 * Error Response definition to send to the requestor if there an error during processing of a request.
 * This is used by ExceptionHandler {@link com.example.demo.forum.exceptions.QuestionsApiExceptionHandler}.
 *
 * @author Sathish Pendem
 */
@Builder
@Getter
public class QuestionsApiErrorResponse {
    private String errorCode;
    private String message;
    private String description;
}
