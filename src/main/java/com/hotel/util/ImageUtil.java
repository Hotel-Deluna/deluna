package com.hotel.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.hotel.company.dto.HotelDto;
import com.hotel.company.dto.HotelMapper;
import com.hotel.company.vo.HotelInfoVo;
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

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    HotelMapper hotelMapper;

    /**
     * AWS 파일 업로드
     * @param multipartFile
     * @return
     */
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

    /**
     * AWS 파일 삭제
     * @param imageList
     */
    public void deleteImage(List<String> imageList) {
        try{
            imageList.forEach(fileName -> {
                DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
                amazonS3Client.deleteObject(request);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getImageUrl(String fileName) {
        return "https://"+bucket+".s3."+region+".amazonaws.com/"+fileName;
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

    /**
     * 이미지 리사이징
     * @param fileName
     * @param fileFormatName
     * @param originalImage
     * @param targetWidth
     * @return
     */
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

    /**
     * 이미지 AWS 업로드, 이미지 리사이징, 이미지 정보 DB 저장
     * @return
     */
    public void insertImage(List<MultipartFile> multipartFile, int selectType, int PK, String userPk) throws Exception{

        List<String> imageUrlList = uploadImage(multipartFile);
        for(int i=0; i < imageUrlList.size(); i++){
            String imageName = imageUrlList.get(i);
            String bucket_url = getImageUrl(imageName);
            HotelDto.ImageTable imageTable = new HotelDto.ImageTable();
            imageTable.setSelect_type(selectType);
            imageTable.setPrimary_key(PK);
            imageTable.setPicture_name(imageName);
            imageTable.setBucket_url(bucket_url);
            imageTable.setPicture_sequence(i+1); // 사진순서 1부터 시작
            imageTable.setInsert_user(userPk);
            imageTable.setUpdate_user(userPk);
            hotelMapper.insertImage(imageTable);
        }

    }

    /**
     * 이미지 정보 조회
     * @param selectType
     * @param PK
     * @return
     */
    public List<String> selectImage(int selectType, int PK) throws Exception{
        List<String> result;
        HotelInfoVo.ImageInfo imageParams = new HotelInfoVo.ImageInfo();
        imageParams.setSelect_type(selectType);
        imageParams.setPrimary_key(PK);
        result = hotelMapper.selectImageList(imageParams);

        return result;
    }

    /**
     * AWS에 저장된 이미지 삭제 & 이미지 테이블 정보 삭제
     * @param selectType
     * @param PK
     * @throws Exception
     */
    public void deleteImage(int selectType, int PK) throws Exception{
        List<String> imageList = selectImage(selectType, PK);
        if(imageList != null){
            deleteImage(imageList);

            HotelDto.ImageTable deleteImageParam = new HotelDto.ImageTable();
            deleteImageParam.setPrimary_key(PK);
            deleteImageParam.setSelect_type(selectType);
            hotelMapper.deleteImage(deleteImageParam);
        }
    }
}
