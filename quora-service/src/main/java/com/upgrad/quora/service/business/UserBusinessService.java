package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

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
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

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
    public UserAuthEntity signIn(final String userName, final String password) throws AuthenticationFailedException {
        UserEntity user_entity = userDao.getUserByUserName(userName);
        if(user_entity == null)
            throw new AuthenticationFailedException("ATH-001","This username does not exist");

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, user_entity.getSalt());

        if(encryptedPassword.equals(user_entity.getPassword())){
           JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
           UserAuthEntity userAuthEntity = new UserAuthEntity();

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

           userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(user_entity.getUuid(),now,expiresAt));
           userAuthEntity.setLoginAt(now);
           userAuthEntity.setExpiresAt(expiresAt);

           userDao.updateAuthToken(userAuthEntity);

            return userAuthEntity;

        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password Failed");
        }

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signOut(String accessToken) throws SignOutRestrictedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        UserEntity userEntity = userAuthEntity.getUser();

        userAuthEntity.setLogoutAt(ZonedDateTime.now());
        userDao.updateAuthToken(userAuthEntity);

        return userEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUser(String uuid, String authorisation) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity user_entity =userDao.getUserByUserUuid(uuid);
        if(user_entity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        UserAuthEntity userAuthEntity = getUserAuth(authorisation);
        if(userAuthEntity.getLogoutAt().isBefore(userAuthEntity.getLoginAt())){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        return  user_entity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity getUserAuth(String authorisation) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(authorisation);
        if(userAuthEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        return userAuthEntity;
    }

}
