package com.hotel.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    AmazonS3Client amazonS3Client;

    public List<String> uploadImage(List<MultipartFile> multipartFile) {
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            // content-type이 image/*가 아닐 경우 해당 루프 진행하지 않음
            if(Objects.requireNonNull(file.getContentType()).contains("image")) {
                String fileName = createFileName(file.getOriginalFilename());
                String fileFormatName = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);

                MultipartFile resizedFile = resizeImage(fileName, fileFormatName, file, 768);

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(resizedFile.getSize());
                objectMetadata.setContentType(file.getContentType());

                try{
                    InputStream inputStream = resizedFile.getInputStream();
                    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                } catch(Exception e) {
                    e.printStackTrace();
                }

                fileNameList.add(fileName);
            }
        });

        return fileNameList;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        String name = "";
        try {
            name = fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return name;
    }

    MultipartFile resizeImage(String fileName, String fileFormatName, MultipartFile originalImage, int targetWidth) {
        try {
            // MultipartFile -> BufferedImage Convert
            BufferedImage image = ImageIO.read(originalImage.getInputStream());
            // newWidth : newHeight = originWidth : originHeight
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if(originWidth < targetWidth)
                return originalImage;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", targetWidth);
            scale.setAttribute("newHeight", targetWidth * originHeight / originWidth);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormatName, baos);
            baos.flush();

            return new MockMultipartFile(fileName, baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
