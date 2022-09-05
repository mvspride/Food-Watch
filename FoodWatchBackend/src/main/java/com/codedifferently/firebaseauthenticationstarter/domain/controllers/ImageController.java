package com.codedifferently.firebaseauthenticationstarter.domain.controllers;

import com.codedifferently.firebaseauthenticationstarter.domain.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

//    @PostMapping("/profile/pic")
//    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
//        logger.info("HIT -/upload | File Name : {}", multipartFile.getOriginalFilename());
//        return imageService.upload(multipartFile);
//    }
//
//    @PostMapping("/profile/pic/{fileName}")
//    public Object download(@PathVariable String fileName) throws IOException {
//        logger.info("HIT -/download | File Name : {}", fileName);
//        return fileService.download(fileName);
//    }


}
