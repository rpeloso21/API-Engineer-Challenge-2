package com.example.demo.service;

import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Image addImageToUser(Long userId, Image image) {
        // Retrieve the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Associate the image with the user
        user.addImage(image);

        // Save the image (or user) to persist the relationship
        // Here, saving the image is enough because of cascade settings.
        return imageRepository.save(image);
    }

    // New method to retrieve images for a specific user
    public List<Image> getImagesForUser(Long userId) {
        // Option 1: Directly query images by user id
        return imageRepository.findByUserId(userId);

        // Option 2: Alternatively, retrieve the user and return its images:
        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new RuntimeException("User not found"));
        // return user.getImages();
    }
}
