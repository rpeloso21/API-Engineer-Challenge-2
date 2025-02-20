package com.example.demo.service;

import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    // Inject the Imgur Client ID from application.properties
    @Value("${imgur.client.id}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();

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

        // Save the image to persist the relationship
        return imageRepository.save(image);
    }

    public List<Image> getImagesForUser(Long userId) {
        return imageRepository.findByUserId(userId);
    }

    public Image uploadImageToUser(Long userId, String base64Image) {
        // Imgur API endpoint for uploading images
        String url = "https://api.imgur.com/3/image";

        // Set up headers with the Client-ID
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare the body with the Base64-encoded image
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64Image);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Call Imgur's API to upload the image
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to upload image to Imgur: " + response.getStatusCode());
        }

        // Extract the "data" portion from the response
        Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("data");

        // Retrieve details from the Imgur response
        String imgurId = (String) responseData.get("id");
        String imgurUrl = (String) responseData.get("link");
        String deleteHash = (String) responseData.get("deletehash");

        // Create a new Image entity using the data from Imgur
        Image image = new Image();
        image.setImgurId(imgurId);
        image.setImgurUrl(imgurUrl);
        image.setDeleteHash(deleteHash);

        // image.setTitle(...);
        // image.setDescription(...);

        // Associate the image with the user in database
        return addImageToUser(userId, image);
    }

    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        String deleteHash = image.getDeleteHash();
        if (deleteHash == null || deleteHash.isEmpty()) {
            throw new RuntimeException("Delete hash not found for image");
        }

        String url = "https://api.imgur.com/3/image/" + deleteHash;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Map.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to delete image from Imgur: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting image from Imgur: " + e.getMessage());
        }

        // Remove the image from the associated user and delete it locally.
        User user = image.getUser();
        if (user != null) {
            user.getImages().remove(image);
            userRepository.save(user);
        }
        imageRepository.delete(image);
    }
}
