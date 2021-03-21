package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author vipin mohan
 * Class implementing the endpoints for the Answer Controller
 */

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    /**
     * @author vipin mohan
     * Class implementing the endpoints for the create Answer
     */


    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/{questionId}/answer/create",
            produces=MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer( final AnswerRequest answerRequest,
                                                       @PathVariable("questionId") final String uuid,
                                                       @RequestHeader("authorization") final String accesstoken)
            throws InvalidQuestionException, AuthenticationFailedException,AuthorizationFailedException {
        String bearerToken = null;
        try {
            bearerToken = accesstoken.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = accesstoken;
        }

        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());

        // Return response with created answer entity
        final AnswerEntity createdAnswerEntity =  answerBusinessService.createAnswer(answerEntity, uuid, bearerToken);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);

    }

    /**
     * @author vipin mohan
     * Class implementing the endpoints for the Edit Answer
     */

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/answer/edit/{answerId}",
            produces=MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> editAnswer(final AnswerRequest answerRequest,
                                                     @PathVariable("answerId") final String uuid,
                                                     @RequestHeader("authorization") final String accesstoken)
            throws AnswerNotFoundException, AuthenticationFailedException, AuthorizationFailedException {
        String bearerToken = null;
        try {
            bearerToken = accesstoken.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = accesstoken;
        }

        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());

        // Return response with created answer entity
        final AnswerEntity editAnswerEntity =  answerBusinessService.editAnswer(answerEntity, uuid, bearerToken);
        AnswerResponse answerResponse = new AnswerResponse().id(editAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);

    }

}
