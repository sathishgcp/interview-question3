package com.example.demo.forum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

/**
 * QuestionDetails
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-09-20T00:54:17.931Z")


@Builder
public class QuestionDetails {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("author")
    private String author = null;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("replies")
    private Long replies = null;


    /**
     * Get id
     *
     * @return id
     **/


    public Long getId() {
        return id;
    }


    /**
     * Get author
     *
     * @return author
     **/


    public String getAuthor() {
        return author;
    }

    /**
     * message text
     *
     * @return message
     **/


    public String getMessage() {
        return message;
    }


    /**
     * Get replies
     *
     * @return replies
     **/


    public Long getReplies() {
        return replies;
    }

}

