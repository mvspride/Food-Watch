package com.codedifferently.firebaseauthenticationstarter.domain.services;

import com.codedifferently.firebaseauthenticationstarter.domain.controllers.UserController;
import com.codedifferently.firebaseauthenticationstarter.domain.exception.FoodNotFoundException;
import com.codedifferently.firebaseauthenticationstarter.domain.exception.UserNotFoundException;
import com.codedifferently.firebaseauthenticationstarter.domain.models.Food;
import com.codedifferently.firebaseauthenticationstarter.domain.models.User;
import com.codedifferently.firebaseauthenticationstarter.domain.repos.FoodRepo;
import com.codedifferently.firebaseauthenticationstarter.domain.repos.UserRepo;
import com.codedifferently.firebaseauthenticationstarter.security.models.FireBaseUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FoodService {

    @Autowired
    FoodRepo foodRepo;

    @Autowired
    UserRepo userRepo;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    public ResponseEntity<String> newFood(FireBaseUser authUser, Food food){
        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        Food newFood;
        String foodName = food.getName();
        try {
            newFood = foodRepo.findByName(food.getName()).orElseThrow(()-> new FoodNotFoundException(foodName));
            return ResponseEntity.ok("You already have a food named " +foodName +". Choose another name");
        }catch (FoodNotFoundException f){
            food.setUser(user);
            food = foodRepo.save(food);
            //user.addToSavedFoods(food);
            user.addToFoodHistory(food);
            userRepo.save(user);
            return ResponseEntity.ok( "added "+food.getName()+"with "+food.getCalories()+" calories");
        }
    }
    public ResponseEntity<String> saveFood(FireBaseUser authUser, String filename, Integer calories){
        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        Food newFood;
        try {
            newFood = foodRepo.findByName(filename).orElseThrow(()-> new FoodNotFoundException(filename));
            return ResponseEntity.ok("You already have a food named " +filename +". Choose another name");
        }catch (FoodNotFoundException f){
            Food food = new Food(filename,calories);
            food = foodRepo.save(food);
            user.addToFoodHistory(food);
            food.setUser(user);
            //logger.info(food.toString());
            //logger.info(user.toString());
            userRepo.save(user);
            foodRepo.save(food);
            return ResponseEntity.ok( "added "+food.getName()+"with "+food.getCalories()+" calories");
        }

    }

//    public void removeFoodFromSaved(FireBaseUser authUser,Food food){
//        foodRepo.delete(food);
//        User user = new User(authUser.getEmail());
//        currentUser.removeFromSavedFoods(food);
//        userRepo.save(currentUser);
//    }

    public ResponseEntity<String> clearFoodHistory(FireBaseUser authUser){
        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        user.clearFoodHistory();
        userRepo.save(user);
        return ResponseEntity.ok( "cleared "+user.getEmail()+" history ");
    }
    public ResponseEntity<String> getFoodHistory(FireBaseUser authUser){
        User user = userRepo.findByEmail(authUser.getEmail()).orElseThrow(()-> new UserNotFoundException(authUser.getEmail()));
        return ResponseEntity.ok(user.getFoodHistory().toString());
    }

    public int calorie(Map<String, Object> FeedBackStatus) throws JSONException {
        // Works Here trying to replicate
        assert FeedBackStatus != null;
        JSONObject Attempt2 = new JSONObject(FeedBackStatus);
        List<Integer> list = new ArrayList<>();
        JSONArray array = Attempt2.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            //list.add(array.getJSONObject(i).getString("items"));
            JSONArray itemsArray = array.getJSONObject(i).getJSONArray("items");
            //System.out.println(array.getJSONObject(i).getString("items"));
            for (int itemsIndex = 0; itemsIndex < itemsArray.length(); itemsIndex++) {
                JSONObject caloriesObj = new JSONObject(itemsArray.getJSONObject(itemsIndex).getString("nutrition"));
                int calories = caloriesObj.getInt("calories");
                // System.out.println(calories);
                list.add(calories);
            }
        }
        Integer toBeReturned = list.get(0);
        int[] example1 = list.stream().mapToInt(i -> i).toArray();
        int length = example1.length;
        return average(example1, length);
    }

    // Function that return average of an array.
    public int average(int a[], int n) {
        // Find sum of array element
        int sum = 0;
        for (int i = 0; i < n; i++)
            sum += a[i];
        return (int) (sum / n);
    }


    public String name(Map<String, Object> FeedBackStatus) throws JSONException {
        assert FeedBackStatus != null;
        JSONObject Attempt2 = new JSONObject(FeedBackStatus);
        List<String> list = new ArrayList<>();
        JSONArray array = Attempt2.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            //list.add(array.getJSONObject(i).getString("items"));
            JSONArray itemsArray = array.getJSONObject(i).getJSONArray("items");
            //System.out.println(array.getJSONObject(i).getString("items"));
            for (int itemsIndex = 0; itemsIndex < itemsArray.length(); itemsIndex++) {
                String caloriesObj = (itemsArray.getJSONObject(itemsIndex).getString("group"));
                //int calories = caloriesObj.getInt("calories");
                // System.out.println(calories);
                list.add(caloriesObj);
            }
        }
        return list.get(0);
    }

}