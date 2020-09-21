package com.example.demo.forum.api;


import com.example.demo.forum.exceptions.QuestionsApiErrorResponse;
import com.example.demo.forum.model.PostQuestionReplyBody;
import com.example.demo.forum.model.PostReplyResponse;
import com.example.demo.forum.model.QuestionDetails;
import com.example.demo.forum.model.QuestionsResponse;
import com.example.demo.forum.persistence.QuestionsEntity;
import com.example.demo.forum.persistence.QuestionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests for the API endpoints defined in {@link com.example.demo.forum.api.QuestionsApi}
 * H2 DB is used as backend db.
 *
 * @author Sathish Pendem
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionsApiControllerTest {

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    @Autowired
    private QuestionsRepository repository;
    private QuestionsEntity sampleQuestion;

    /**
     * Initializes the test data before any tests are run.
     * Configures the rest content-type and creates an initial question to use in tests.
     */
    @Before
    public void init() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        sampleQuestion = repository.save(QuestionsEntity.builder()
                .message("test message")
                .author("test user")
                .build());
    }

    /**
     * Create a new Question in the forum.
     *
     * @result New Question is created with input data.
     */
    @Test
    public void addQuestion_whenInputIsValid_ReturnsNewQuestionCreated() {
        HttpEntity<PostQuestionReplyBody> entity = new HttpEntity<PostQuestionReplyBody>(PostQuestionReplyBody.builder()
                .message("test message")
                .author("sathish")
                .build(),
                headers);
        ResponseEntity<QuestionDetails> response = restTemplate.exchange(
                "/questions",
                HttpMethod.POST, entity, QuestionDetails.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isGreaterThan(1l);
        assertThat(response.getBody().getAuthor()).isEqualTo("sathish");
    }

    /**
     * Exception occurs while creating the new question.
     *
     * @result Response contains a validation error. Also tests the Exception handler defined.
     */
    @Test
    public void addQuestion_whenInputAuthorIsEmpty_ReturnsErrorResponse() {
        HttpEntity<PostQuestionReplyBody> entity = new HttpEntity<PostQuestionReplyBody>(PostQuestionReplyBody.builder()
                .message("test message")
                .author("")
                .build(),
                headers);
        ResponseEntity<QuestionsApiErrorResponse> response = restTemplate.exchange(
                "/questions",
                HttpMethod.POST, entity, QuestionsApiErrorResponse.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERROR008");
        assertThat(response.getBody().getMessage()).contains("Invalid input. Author cannot be empty or null.");
    }

    /**
     * Create new reply with the input data.
     *
     * @result New reply is created for given question.
     */
    @Test
    public void createReply_whenInputIsValid_ReturnsNewReplyCreated() {
        HttpEntity<PostQuestionReplyBody> entity = new HttpEntity<>(PostQuestionReplyBody.builder()
                .message("test message")
                .author("sathish")
                .build(),
                headers);
        ResponseEntity<PostReplyResponse> response = restTemplate.exchange(
                "/questions/" + sampleQuestion.getId() + "/reply",
                HttpMethod.POST, entity, PostReplyResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAuthor()).isEqualTo("sathish");
        assertThat(response.getBody().getMessage()).contains("test");
        assertThat(response.getBody().getQuestionId()).isEqualTo(sampleQuestion.getId());
    }

    /**
     * Tests exception scenario for createReply endpoint.
     * Input data has a validation error.
     *
     * @result Error response is received with a validation error.
     */
    @Test
    public void createReply_whenInputMessageIsEmpty_ReturnsErrorResponse() {
        HttpEntity<PostQuestionReplyBody> entity = new HttpEntity<>(PostQuestionReplyBody.builder()
                .message("")
                .author("sathish")
                .build(),
                headers);
        ResponseEntity<QuestionsApiErrorResponse> response = restTemplate.exchange(
                "/questions/" + sampleQuestion.getId() + "/reply",
                HttpMethod.POST, entity, QuestionsApiErrorResponse.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERROR009");
        assertThat(response.getBody().getMessage()).contains("Invalid input. Message cannot be empty or null");
    }

    /**
     * Test getQuestions to receive all questions saved in DB.
     *
     * @result Returns a list of questions saved in DB.
     */
    @Test
    public void getListOfQuestions_whenInputIsValid_ReturnsQuestionsList() {
        HttpEntity<?> entity = new HttpEntity<>(null,
                headers);
        ResponseEntity<List<QuestionDetails>> response = restTemplate.exchange(
                "/questions/",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<QuestionDetails>>() {
                });
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);
        assertThat(response.getBody().get(0)).isNotNull();
    }

    /**
     * Request to get input question details. Input takes existing questionId.
     *
     * @result Response is validated for question details requested by questionID.
     */
    @Test
    public void getQuestionDetails_whenInputIsValid_ReturnsQuestionsDetails() {
        HttpEntity<?> entity = new HttpEntity<>(null,
                headers);
        ResponseEntity<QuestionsResponse> response = restTemplate.exchange(
                "/questions/" + sampleQuestion.getId(),
                HttpMethod.GET, entity, QuestionsResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getReplies().size()).isGreaterThanOrEqualTo(0);
        assertThat(response.getBody().getId()).isEqualTo(sampleQuestion.getId());
        assertThat(response.getBody().getMessage()).isEqualTo(sampleQuestion.getMessage());
    }
}
