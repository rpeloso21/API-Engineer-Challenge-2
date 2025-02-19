package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // The Imgur image id (or URL) to identify the image in Imgur's system
    private String imgurId;
    private String imgurUrl;
    
    // Optional: any additional metadata you want to store
    private String title;
    private String description;
    
    // Each image is associated with one user.
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Constructors
    public Image() {}

    public Image(String imgurId, String imgurUrl, String title, String description, User user) {
        this.imgurId = imgurId;
        this.imgurUrl = imgurUrl;
        this.title = title;
        this.description = description;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getImgurId() {
        return imgurId;
    }

    public void setImgurId(String imgurId) {
        this.imgurId = imgurId;
    }

    public String getImgurUrl() {
        return imgurUrl;
    }

    public void setImgurUrl(String imgurUrl) {
        this.imgurUrl = imgurUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
