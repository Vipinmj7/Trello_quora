package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.User_Entity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method for user signup.This method checks for the entry of user in database with first name or email.
     * If there is entry in table then throws proper exception
     * @param userEntity
     * @return
     * @throws SignUpRestrictedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public User_Entity signup(User_Entity userEntity) throws SignUpRestrictedException {

        if(userDao.getUserByName(userEntity.getFirstName())!=null) {
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }

        if (userDao.getUserByEmail(userEntity.getEmail()) != null)
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");

        String[] encryptedTxt = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedTxt[0]);
        userEntity.setPassword(encryptedTxt[1]);
        return userDao.createUser(userEntity);

    }

    /**
     * Method for the validation of authentication
     * @param userName
     * @param password
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuth signIn(final String userName, final String password) throws AuthenticationFailedException {
        User_Entity user_entity = userDao.getUserByUserName(userName);
        if(user_entity == null)
            throw new AuthenticationFailedException("ATH-001","This username does not exist");

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, user_entity.getSalt());
        if(encryptedPassword.equals(user_entity.getPassword())){

        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password Failed");
        }
        return null;
    }
}
