package com.example.demo.utils;

import com.example.demo.forum.api.QuestionsApi;
import com.example.demo.forum.delegates.QuestionControllerDelegate;
import com.example.demo.forum.model.PostQuestionReplyBody;
import com.example.demo.forum.persistence.QuestionsEntity;

import java.util.Collections;

/**
 * Factory to create models and entities to use in test cases.
 * It provides static methods to create specific model instances.
 * @author Sathish Pendem.
 */
public class MockObjectFactory {

    public static QuestionsEntity createQuestionsEntity() {
        return QuestionsEntity.builder()
                .message("first message")
                .author("sample")
                .id(101l)
                .replies(Collections.singletonList(createReplyEntity()))
                .build();
    }

    public static PostQuestionReplyBody getRequestWithEmptyAuthor() {
        return PostQuestionReplyBody.builder()
                .message("sample message")
                .build();
    }

    public static PostQuestionReplyBody getRequestWithEmptyMessage() {
        return PostQuestionReplyBody.builder()
                .author("Sathish")
                .build();
    }

    public static PostQuestionReplyBody getValidRequestBody() {
        return PostQuestionReplyBody.builder()
                .author("Sathish")
                .message("sample message")
                .build();
    }

    public static QuestionsEntity createReplyEntity() {
        return QuestionsEntity.builder()
                .message("first message")
                .author("sample")
                .parentQuestionId(10l)
                .id(101l)
                .question(QuestionsEntity.builder().id(10l).build())
                .build();
    }
}
