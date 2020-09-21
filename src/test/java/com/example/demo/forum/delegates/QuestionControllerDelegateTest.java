package com.example.demo.forum.delegates;

import com.example.demo.forum.exceptions.QuestionsApiException;
import com.example.demo.forum.model.PostReplyResponse;
import com.example.demo.forum.model.QuestionDetails;
import com.example.demo.forum.model.QuestionsResponse;
import com.example.demo.forum.persistence.QuestionsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.MockObjectFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test for delegate {@link QuestionControllerDelegate} which tests all code logics defined in the public methods..
 *
 * @author Sathish Pendem
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionControllerDelegateTest {

    @InjectMocks
    private QuestionControllerDelegate controllerDelegate;
    @Mock
    private QuestionsRepository repository;

    /**
     * Test add new question with valid input to test success flow.
     *
     * @result New Question is created with input data.
     */
    @Test
    public void addQuestion_WhenInputIsValid_ReturnNewQuestionCreated() {
        when(repository.save(any())).thenReturn(createQuestionsEntity());
        ResponseEntity<QuestionDetails> response = controllerDelegate.addQuestion(getValidRequestBody());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(101l);
    }

    /**
     * Test add new question when input has invalid author input.
     *
     * @result method throws exception and the same is validated.
     */
    @Test
    public void addQuestion_WhenInputAuthorIsEmpty_ThrowsException() {
        assertThatThrownBy(() -> controllerDelegate.addQuestion(getRequestWithEmptyAuthor()))
                .hasMessageContaining("Invalid input. Author cannot be empty or null")
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test add new question when input has invalid message input.
     *
     * @result method throws exception and the same is validated.
     */
    @Test
    public void addQuestion_WhenInputMessageIsEmpty_ThrowsException() {
        assertThatThrownBy(() -> controllerDelegate.addQuestion(getRequestWithEmptyMessage()))
                .hasMessageContaining("Invalid input. Message cannot be empty or null")
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test add new question when there is a db operation failure.
     *
     * @result method throws exception and the same is validated.
     */
    @Test
    public void addQuestion_WhenRepositoryFails_ThrowsException() {
        when(repository.save(any())).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.addQuestion(getValidRequestBody()))
                .hasMessageContaining("Exception occurred while saving Question to Database")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test add new reply when input is valid and new reply is created.
     *
     * @result new reply created and the same is verified here.
     */
    @Test
    public void createReply_WhenInputIsValid_createsNewReply() {
        when(repository.save(any())).thenReturn(createReplyEntity());
        when(repository.findById(any())).thenReturn(Optional.of(createReplyEntity()));
        ResponseEntity<PostReplyResponse> response = controllerDelegate.createReply(10l, getValidRequestBody());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(101l);
        assertThat(response.getBody().getQuestionId()).isEqualTo(10l);
    }

    /**
     * Test add new reply when input question is not found.
     *
     * @result throws error and the same is validated.
     */
    @Test
    public void createReply_whenInputQuestionIsNotFound_throwsException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controllerDelegate.createReply(10l, getValidRequestBody()))
                .hasMessageContaining("Question Not Found.")
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test add new reply when input has invalid author input.
     *
     * @result throws validation error and the result is validated.
     */
    @Test
    public void createReply_whenInputAuthorIsEmpty_throwsException() {
        assertThatThrownBy(() -> controllerDelegate.createReply(10l, getRequestWithEmptyAuthor()))
                .hasMessageContaining("Invalid input. Author cannot be empty or null")
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test add new reply when input has invalid message input.
     *
     * @result throws validation error and the result is validated.
     */
    @Test
    public void createReply_WhenInputMessageIsEmpty_ThrowsException() {
        assertThatThrownBy(() -> controllerDelegate.createReply(10l, getRequestWithEmptyMessage()))
                .hasMessageContaining("Invalid input. Message cannot be empty or null")
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test add new reply when jpa error is thrown.
     *
     * @result throws jpa exception and the result is validated.
     */
    @Test
    public void createReply_WhenJPAExceptionOccurs_ThrowsException() {
        when(repository.findById(any())).thenReturn(Optional.of(createReplyEntity().getQuestion()));
        when(repository.save(any())).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.createReply(10l, getValidRequestBody()))
                .hasMessageContaining("JPA exception while saving reply")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test getListOfQuestion service when no error occurs.
     *
     * @result return a list of questions and the result is validated.
     */
    @Test
    public void getListOfQuestions_whenSuccessful_returnsListOfQuestions() {
        when(repository.findByParentQuestionIdIsNull())
                .thenReturn(Collections.singletonList(createQuestionsEntity()));
        ResponseEntity<List<QuestionDetails>> response = controllerDelegate.getListOfQuestions();
        assertThat(response).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0)).isNotNull();
        assertThat(response.getBody().get(0).getId()).isEqualTo(101l);
    }

    /**
     * Test getListofQuestions service and the db throws error.
     *
     * @result exception is thrown by the method.
     */
    @Test
    public void getListOfQuestions_whenJPAExceptionOccurs_throwsException() {
        when(repository.findByParentQuestionIdIsNull()).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.getListOfQuestions())
                .hasMessageContaining("Exception occurred while reading Questions from Database")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test getQuestionDetails api with valid input.
     *
     * @result return the question details for input questionId.
     */
    @Test
    public void getQuestionDetails_whenInputIsValid_returnsValidResponse() {
        when(repository.findById(any())).thenReturn(Optional.of(createQuestionsEntity()));
        ResponseEntity<QuestionsResponse> response = controllerDelegate.getQuestionDetails(10l);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(101l);
        assertThat(response.getBody().getReplies().size()).isEqualTo(1);
    }

    /**
     * Test getQuestionDetails api when  input question id does not exist.
     *
     * @result not found error is thrown by method.
     */
    @Test
    public void getQuestionDetails_whenInputQuestionIsNotFound_throwsException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controllerDelegate.getQuestionDetails(10l))
                .hasMessageContaining("Question Not Found.")
                .isInstanceOf(QuestionsApiException.class);
    }

    /**
     * Test getQuestionDetails api when a db error occurs.
     *
     * @result JPA exception is thrown by the method.
     */
    @Test
    public void getQuestionDetails_whenJPAExceptionOccurs_throwsException() {
        when(repository.findById(any())).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.getQuestionDetails(10l))
                .hasMessageContaining("Exception occurred while reading question details")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsApiException.class);
    }
}
