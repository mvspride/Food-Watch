package com.codedifferently.firebaseauthenticationstarter.domain.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Food> savedFoods = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Food> foodHistory = new ArrayList<>();

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public List<Food> getSavedFoods() {
//        return this.savedFoods;
//    }
//
//    public void addToSavedFoods(Food food){
//        this.savedFoods.add(food);
//        System.out.println(savedFoods.toString()+ "  saved   " +savedFoods.size());
//    }
//
//    public void removeFromSavedFoods(Food food){
//        this.savedFoods.remove(food);
//    }
//    public void clearSavedFoods(){
//        this.savedFoods.clear();
//    }
//
//    public void setSavedFoods(List<Food> savedFoods) {
//        this.savedFoods = savedFoods;
//    }

    public List<Food> getFoodHistory() {
        return this.foodHistory;
    }

    public void addToFoodHistory(Food food){
        System.out.println(foodHistory.toString()+ "  history   " +savedFoods.size());
        this.foodHistory.add(food);
    }
    public void clearFoodHistory(){
        this.foodHistory.clear();
    }

    public void setFoodHistory(List<Food> foodHistory) {
        this.foodHistory = foodHistory;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", savedFoods=" + savedFoods +
                ", foodHistory=" + foodHistory +
                '}';
    }
}
