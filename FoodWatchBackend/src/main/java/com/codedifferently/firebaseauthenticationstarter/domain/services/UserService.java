package com.codedifferently.firebaseauthenticationstarter.domain.services;

import com.codedifferently.firebaseauthenticationstarter.domain.exception.UserNotFoundException;
import com.codedifferently.firebaseauthenticationstarter.domain.models.Food;
import com.codedifferently.firebaseauthenticationstarter.domain.models.User;
import com.codedifferently.firebaseauthenticationstarter.domain.repos.UserRepo;
import com.codedifferently.firebaseauthenticationstarter.security.models.FireBaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepo userRepo;

    public User save(FireBaseUser authUser){
        User user;
        try{
            user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        }catch (UserNotFoundException u){
            user = new User(authUser.getEmail());
            user =  userRepo.save(user);
            logger.info(user.toString());
            return user;
        }
       return user;
    }

    public User getUser(FireBaseUser authUser){
        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        logger.info(user.toString());
        return user;
    }

    public List<User> getAllUsers(FireBaseUser authUser){
        List<User> users = userRepo.findAll();
        logger.info(users.toString());
        return users;
    }

//    public void clearUserSaved(FireBaseUser authUser){
//        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
//        user.clearSavedFoods();
//        userRepo.save(user);
//    }
//    public List<Food> getUserSaved(FireBaseUser authUser){
//        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
//        return user.getSavedFoods();
//    }

    public List<Food> getUserHistory(FireBaseUser authUser){
        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        return user.getFoodHistory();
    }
}
