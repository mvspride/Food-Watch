package com.codedifferently.firebaseauthenticationstarter.domain.models;

import javax.persistence.*;

@Entity
@Table(name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private Integer calories;

    @ManyToOne
    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public Food() {
    }

    public Food(String filename, Integer calories) {
        this.name = filename;
        this.calories = calories;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", user=" + user.getEmail() +
                '}';
    }

}
