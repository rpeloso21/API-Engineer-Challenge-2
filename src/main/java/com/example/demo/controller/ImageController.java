package com.example.demo.controller;

import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // Get all images associated with a specified user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Image>> getImagesForUser(@PathVariable Long userId) {
        List<Image> images = imageService.getImagesForUser(userId);
        return ResponseEntity.ok(images);
    }

    // Upload an image and associate with userId (stores in Imgur and in H2 database)
    @PostMapping("/upload/user/{userId}")
    public ResponseEntity<Image> uploadImageToUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> payload) {
        // Expecting the payload to have a key "base64Image"
        String base64Image = payload.get("base64Image");
        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Image uploadedImage = imageService.uploadImageToUser(userId, base64Image);
        return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED);
    }

    // Delete image from Imgur and form H2 database
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}
