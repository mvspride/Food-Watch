package com.codedifferently.firebaseauthenticationstarter.domain.repos;

import com.codedifferently.firebaseauthenticationstarter.domain.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepo extends JpaRepository<Food,String> {
    Optional<Food> findByName(String name);
}
