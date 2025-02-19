package com.example.demo.controller;

import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // For testing: add an image to a user
    @PostMapping("/user/{userId}")
    public ResponseEntity<Image> addImageToUser(
            @PathVariable Long userId,
            @RequestBody Image image) {
        Image savedImage = imageService.addImageToUser(userId, image);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Image>> getImagesForUser(@PathVariable Long userId) {
        List<Image> images = imageService.getImagesForUser(userId);
        return ResponseEntity.ok(images);
    }
}
