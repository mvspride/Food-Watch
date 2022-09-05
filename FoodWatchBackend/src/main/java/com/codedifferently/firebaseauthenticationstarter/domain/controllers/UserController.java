package com.codedifferently.firebaseauthenticationstarter.domain.controllers;

import com.codedifferently.firebaseauthenticationstarter.domain.models.User;
import com.codedifferently.firebaseauthenticationstarter.domain.repos.UserRepo;
import com.codedifferently.firebaseauthenticationstarter.domain.services.UserService;
import com.codedifferently.firebaseauthenticationstarter.security.models.FireBaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
//@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(com.codedifferently.firebaseauthenticationstarter.domain.controllers.UserController.class);

    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal FireBaseUser authUser) {
        //logger.info("A request was made by user with id {} and email {}",user.getUid(), user.getName());
        return ResponseEntity.ok(userService.getUser(authUser));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@AuthenticationPrincipal FireBaseUser authUser) {
        //logger.info("A request was made by user with id {} and email {}",user.getUid(), user.getName());
        return ResponseEntity.ok(userService.getAllUsers(authUser));
    }


    @PostMapping("/newuser")
    public ResponseEntity<User> createUser(@AuthenticationPrincipal FireBaseUser fireBaseUser){
        User user = userService.save(fireBaseUser);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/verify")
    public ResponseEntity<String> getIdToken(@RequestBody FireBaseUser user) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword"))
                .header("X-RapidAPI-Key", "AIzaSyCRjzt4iwZ0AyCT4pVbKik0DTAvkh1Uj_U")
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return ResponseEntity.ok(response.body());
    }


}
