package com.codedifferently.firebaseauthenticationstarter.domain.controllers;

import com.codedifferently.firebaseauthenticationstarter.domain.models.Food;
import com.codedifferently.firebaseauthenticationstarter.domain.models.User;
import com.codedifferently.firebaseauthenticationstarter.domain.services.FoodService;
import com.codedifferently.firebaseauthenticationstarter.domain.services.UserService;
import com.codedifferently.firebaseauthenticationstarter.security.models.FireBaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FoodController {
    @Autowired
    FoodService foodService;

    @Autowired
    UserService userService;


    @GetMapping("/history")
    public ResponseEntity<String> getUserHistory(@AuthenticationPrincipal FireBaseUser authUser){
        return foodService.getFoodHistory(authUser);
    }
    @DeleteMapping("/history")
    public ResponseEntity<String> clearUserHistory(@AuthenticationPrincipal FireBaseUser authUser){
        return foodService.clearFoodHistory(authUser);
    }
    @PostMapping("/newFood")
    public ResponseEntity<String> createFood(@AuthenticationPrincipal FireBaseUser authUser, @RequestBody Food food){
        return foodService.newFood(authUser,food);

    }

//    @DeleteMapping("/saveFood")
//    public ResponseEntity<String> clearSavedFood(@AuthenticationPrincipal FireBaseUser authUser){
//        userService.clearUserSaved(authUser);
//        return new ResponseEntity<>("cleared "+authUser.getEmail()+" saved foods ", HttpStatus.OK);
//    }
//
//    // public ResponseEntity<String> getFood(@PathVariable Long )
//
//    @GetMapping("/saveFood")
//    public List<Food> getSavedFood(@AuthenticationPrincipal FireBaseUser authUser){
//        return userService.getUserSaved(authUser);
//    }



}
