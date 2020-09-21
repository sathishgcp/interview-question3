package com.example.demo.forum.api;

import com.example.demo.forum.delegates.QuestionControllerDelegate;
import com.example.demo.forum.model.PostQuestionReplyBody;
import com.example.demo.forum.model.PostReplyResponse;
import com.example.demo.forum.model.QuestionDetails;
import com.example.demo.forum.model.QuestionsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-09-20T00:54:17.931Z")
/**
 * QuestionsApiController component extends {@link QuestionsApi} interface and
 * implements all rest services.
 * This component handovers the actual implementation to the delegate {@link QuestionControllerDelegate}
 * The delegate is injected via Constructor.
 * @generated SwaggerIO.
 */
@Controller
@RequiredArgsConstructor
public class QuestionsApiController implements QuestionsApi {

    private static final Logger log = LoggerFactory.getLogger(QuestionsApiController.class);

    private final QuestionControllerDelegate delegate;

    public ResponseEntity<QuestionDetails> addQuestion(@Valid @RequestBody(required = true) PostQuestionReplyBody body) {
        return delegate.addQuestion(body);
    }

    public ResponseEntity<PostReplyResponse> createReply(@PathVariable("questionId") Long questionId, @Valid @RequestBody PostQuestionReplyBody requestBody) {
        return delegate.createReply(questionId, requestBody);
    }

    public ResponseEntity<List<QuestionDetails>> getListOfQuestions() {
        return delegate.getListOfQuestions();
    }

    public ResponseEntity<QuestionsResponse> getQuestionDetails(@PathVariable("questionId") Long questionId) {
        return delegate.getQuestionDetails(questionId);

    }

}
