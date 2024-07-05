//package com.sparta.sns.image;
//
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class FileHandler {
//
//    public List<Image> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception {
//        // 반환할 파일 리스트
//        List<Image> images = new ArrayList<>();
//
//        // 파일명을 업로드 한 시각으로 변환하여 저장
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String extension = StringUtils.getFilenameExtension(originalFilename);
//         "file_" + timestamp + "." + extension;
//
//        // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
//        // 경로 구분자 File.separator 사용
//        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
//
//        // 파일을 저장할 세부 경로 지정
//        String path = "images" + File.separator + current_date;
//        File file = new File(path);
//
//        // 디렉터리가 존재하지 않을 경우
//        if (!file.exists()) {
//            boolean wasSuccessful = file.mkdirs();
//
//            // 디렉터리 생성에 실패했을 경우
//            if (!wasSuccessful)
//                System.out.println("file: was not successful");
//        }
//
//        // 다중 파일 처리
//        for (MultipartFile file : multipartFiles) {
//
//            // 파일의 확장자 추출
//            String originalFileExtension;
//            String contentType = file.getContentType();
//
//            // 확장자명이 존재하지 않을 경우 처리 x
//            if (ObjectUtils.isEmpty(contentType)) {
//                break;
//            } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
//                if (contentType.contains("image/jpeg"))
//                    originalFileExtension = ".jpg";
//                else if (contentType.contains("image/png"))
//                    originalFileExtension = ".png";
//                else  // 다른 확장자일 경우 처리 x
//                    break;
//            }
//
//            // 파일명 중복 피하고자 나노초까지 얻어와 지정
//            String new_file_name = System.nanoTime() + originalFileExtension;
//
//            // 파일 DTO 생성
//            ImageDto imageDto = ImageDto.builder()
//                    .origFileName(file.getOriginalFilename())
//                    .filePath(path + File.separator + new_file_name)
//                    .fileSize(file.getSize())
//                    .build();
//
//            // 파일 DTO 이용하여 Image 엔티티 생성
//            Image image = new Image(
//                    imageDto.getOrigFileName(),
//                    imageDto.getFilePath(),
//                    imageDto.getFileSize()
//            );
//
//            // 생성 후 리스트에 추가
//            images.add(image);
//
//            // 업로드 한 파일 데이터를 지정한 파일에 저장
//            file = new File(absolutePath + path + File.separator + new_file_name);
//            file.transferTo(file);
//
//            // 파일 권한 설정(쓰기, 읽기)
//            file.setWritable(true);
//            file.setReadable(true);
//        }
//    }
//
//        return images;
//}
//}