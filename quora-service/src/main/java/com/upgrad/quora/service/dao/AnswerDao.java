package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * DAO class handling the CRUD operations on answers
 * @author Vipin mohan
 */
@Repository
public class AnswerDao {

    @PersistenceContext
    private  EntityManager entityManager;

    /**
     * Merge the answer object in the database
     * @param answerEntity
     * @return : answerEntity
     */
    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    /**
     * Persists the answer object in the database
     * @param answerEntity
     * @return : answerEntity
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Fetch the answer object from database using UUID
     * @param uuid
     * @return : questionEntity
     */
    public AnswerEntity getAnswerByUuId(String  uuid) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
