package com.sparta.sns.primary.image.repository;

import com.sparta.sns.primary.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
