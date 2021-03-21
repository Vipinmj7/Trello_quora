package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * This class gives information to the controller class what messeges to be displayed  when exception thrown by service class
 */
@ControllerAdvice

public class RestExceptionHandler {
    /**
     *
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestriction(SignUpRestrictedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.CONFLICT);
    }

    /**
     * Method for telling controller class to display custom message when AuthenticationFailedException thrown by service class
     * @param exe
     * @param request
     * @return
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailed(AuthenticationFailedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }

    /**
     *
     * @param exe
     * @param request
     * @return ErrorResponse
     */

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }

    /**
     *
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.FORBIDDEN);
    }


    /**
     * Handler for InvalidQuestionException
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exception, WebRequest request)  {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).
                message(exception.getErrorMessage()), HttpStatus.NOT_FOUND);
    }
    /**
     * Handler for AnswerNotFoundException
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND);
    }
}
