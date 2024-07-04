package com.sparta.sns.image.service;

import com.sparta.sns.exception.FileStorageException;
import com.sparta.sns.image.entity.Image;
import com.sparta.sns.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;

    @Transactional
    public List<Image> upload(List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = generateFileName(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            // 파일 업로드
            try {
                file.transferTo(filePath);
            } catch (IOException e) {
                throw new FileStorageException("파일 업로드에 실패했습니다.", e);
            }
            // Image 엔티티 생성 및 저장
            Image image = Image.of(fileName, filePath.toString());
            images.add(imageRepository.save(image));
        }
        return images;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

}
