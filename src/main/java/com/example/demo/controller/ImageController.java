package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.model.Image;
import com.example.demo.service.ImageService;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;
import com.example.demo.controller.LoginRequest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ImageController {
    
    private final UserService userService;
    private final ImageService imageService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public ImageController(UserService userService,
                          ImageService imageService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.imageService = imageService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // Upload an image and associate with userId (stores in Imgur and in H2 database)
    @PostMapping("/upload/{userId}")
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

    // Get all images associated with a specific user
    @GetMapping("/image/{userId}")
    public ResponseEntity<List<Image>> getImagesForUser(@PathVariable Long userId) {
        List<Image> images = imageService.getImagesForUser(userId);
        return ResponseEntity.ok(images);
    }
    

    // Delete image endpoint for testing: deletes an image by ID (both in H2 and via Imgur)
    @DeleteMapping("image/{imageId}")
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
