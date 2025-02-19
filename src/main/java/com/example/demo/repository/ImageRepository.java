package com.example.demo.repository;

import com.example.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Custom query method to get images by the associated user's ID
    List<Image> findByUserId(Long userId);
}
