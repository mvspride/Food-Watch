package com.codedifferently.firebaseauthenticationstarter.domain.controllers;

import com.codedifferently.firebaseauthenticationstarter.domain.exception.WrongImageType;
import com.codedifferently.firebaseauthenticationstarter.domain.models.User;
import com.codedifferently.firebaseauthenticationstarter.domain.services.ImageService;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.codedifferently.firebaseauthenticationstarter.domain.services.FoodService;
import com.codedifferently.firebaseauthenticationstarter.security.models.FireBaseUser;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@RestController
public class ApiController {
    @Autowired
    ImageService imageService;
    @Autowired
    ImageController imageController;
    @Autowired
    FoodService foodService;


    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    public ApiController() {
    }


    /**
     * post image to Calorie Mama Api and retrieve it's data
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(
            value = {"/calories"},
            produces = {"application/json"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity<String> getCalories(@RequestParam("file") MultipartFile file,@AuthenticationPrincipal FireBaseUser authUser) throws IOException, InterruptedException {
        //file = imageController.resizeImage(file);
        BufferedImage image = ImageIO.read(file.getInputStream());
        try {
            logger.info("file size = {} x {}, type = ", image.getWidth(), image.getHeight());

        } catch (Exception e) {
            return ResponseEntity.ok(new WrongImageType().toString());
        }
        image = imageService.scaleDown(image, 544, 544);
        String fileType = file.getOriginalFilename().split("\\.")[1];
        file = imageService.imageToFile(imageService.convertImage(image, fileType), file.getOriginalFilename());


        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap();
        parameters.add("file", new FileSystemResource(this.imageService.multipartToFile(file, file.getName())));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");
        new HashMap();
        Map<String, Object> FeedBackStatus = (Map) (new RestTemplate()).exchange("https://api-2445582032290.production.gw.apicast.io/v1/foodrecognition?user_key=34e0a1cb16f774eec562ec24d9a3d3ae", HttpMethod.POST, new HttpEntity(parameters, headers), Map.class, new Object[0]).getBody();
        String fileName = null;
        Integer calorie = null;
        try {
            calorie = foodService.calorie(FeedBackStatus);
            fileName = foodService.name(FeedBackStatus);
            imageService.upload(file, fileName, calorie);
            foodService.saveFood(authUser,fileName,calorie);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(fileName + " has "+ calorie + " calories", HttpStatus.OK);
    }

}

