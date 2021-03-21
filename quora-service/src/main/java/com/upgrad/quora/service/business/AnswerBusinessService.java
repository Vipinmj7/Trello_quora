package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AnswerBusinessService {

    @Autowired
    private  AnswerDao answerDao;
    @Autowired
    private  UserDao userDao;
    @Autowired
    private  QuestionDao questionDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method for the creating answer
     * @param uuid
     * @param accesstoken
     * @return answerEntity
     * @throws AuthenticationFailedException
     * @throws InvalidQuestionException
     * @throws AuthorizationFailedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public  AnswerEntity createAnswer(final AnswerEntity answerEntity, final String uuid, final String accesstoken)
            throws InvalidQuestionException,AuthenticationFailedException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accesstoken);
        QuestionEntity questionEntity = questionDao.getQuestionByuuId(uuid);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        UserAuthEntity userAuthEntity1 = userDao.getUserAuthByToken(accesstoken);
        if(userAuthEntity1.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setQuestion(questionEntity);

        return answerDao.createAnswer(answerEntity);

    }

    /**
     * Method for the editing answer
     * @param uuid
     * @param accesstoken
     * @return answerEntity
     * @throws AnswerNotFoundException
     * @throws AuthorizationFailedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final AnswerEntity answerEntity,final String uuid,final String accesstoken)
    throws AuthorizationFailedException,AnswerNotFoundException{
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accesstoken);
        AnswerEntity answerEntity1 = answerDao.getAnswerByUuId(uuid);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (answerEntity1 == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        UserAuthEntity userAuthEntity1 = userDao.getUserAuthByToken(accesstoken);
        if(userAuthEntity1.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }
        UserEntity currentUser = userAuthEntity.getUser();
        if (currentUser.getId() != answerEntity1.getUser().getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }

        answerEntity.setUuid(answerEntity1.getUuid());
        answerEntity.setDate(answerEntity1.getDate());
        answerEntity.setUser(answerEntity1.getUser());
        answerEntity.setAns(answerEntity.getAns());
        answerEntity.setQuestion(answerEntity1.getQuestion());
        answerEntity.setId(answerEntity1.getId());

        return answerDao.editAnswer(answerEntity);

    }
}

