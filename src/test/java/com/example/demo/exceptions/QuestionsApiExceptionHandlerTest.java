package com.example.demo.exceptions;

import com.example.demo.forum.delegates.QuestionControllerDelegate;
import com.example.demo.forum.exceptions.QuestionsApiException;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

/**
 * Unit test for Controller advice defined {@link com.example.demo.forum.exceptions.QuestionsApiExceptionHandler}.
 * It tests all exception handlers defined in the controller Advice.
 *
 * @author Sathish Pendem
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class QuestionsApiExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuestionControllerDelegate delegate;

    /**
     * Test case handles {@link QuestionsApiException} raised by the api and generates
     * {@link com.example.demo.forum.exceptions.QuestionsApiErrorResponse} to the user.
     *
     * @result {@link QuestionsApiException } is handled by exception handler.
     */
    @Test
    public void testHandler_whenQuestionsApiExceptionIsThrown_receivesErrorResponse() throws Exception {
        when(delegate.getQuestionDetails(2l)).thenThrow(
                new QuestionsApiException("Sample error", null, "ERROR0001", HttpStatus.UNPROCESSABLE_ENTITY)
        );
        mvc.perform(MockMvcRequestBuilders.get("/questions/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode", Is.is("ERROR0001")));
    }

    /**
     * Test case handles all exceptions other than {@link QuestionsApiException} raised by the api and generates
     * {@link com.example.demo.forum.exceptions.QuestionsApiErrorResponse} to the user.
     *
     * @result {@link RuntimeException} generated by the delegate is handled here.
     */
    @Test
    public void testHandler_whenUnknownExceptionIsThrown_receivesErrorResponse() throws Exception {
        when(delegate.getQuestionDetails(2l)).thenThrow(
                new RuntimeException("Unknown Exception.")
        );
        mvc.perform(MockMvcRequestBuilders.get("/questions/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode", Is.is("ERROR000")));
    }

}
