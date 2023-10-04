
package com.ssafy.special.service.food;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ssafy.special.domain.member.Member;
import com.ssafy.special.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    // S3 버킷 정보. (버킷 - S3 저장소 이름이라고 생각하면 됨)
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;


    @Transactional
    public void uploadImg(String email, MultipartFile file) throws Exception {

        try {
            // 파일에 대한 메타 정보 설정 ( 타입, 사이즈 )
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // S3에 저장할 파일 이름을 만든다.
            String S3_fileName = "userImg/" + getRandomFileName();

            // S3 해당 "bucket"에 "S3_fileName"이름으로 "파일 내용", "파일 정보"를 업로드한다.
            amazonS3Client.putObject(bucket, S3_fileName, file.getInputStream(), metadata);

            // 만약 download 할거라면 original_fileName - S3_fileName 으로 DB에 저장해놓음.
            // 그리고 original_fileName으로 DB에서 조회해서 S3_fileName 가져오고 그 값으로 S3에서 가져옴
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

            member.setImg(S3_fileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("문제 발생~!");
        }

    }

    public ResponseEntity<byte[]> getFoodImg(int foodSeq) throws IOException {

        String S3_fileName = "foodImg/" + foodSeq + ".jpg";

        // S3 해당 bucket에서 해당 이름으로 저장된 이미지를 가져옴.
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, S3_fileName));

        // 파일에 대한 정보를 헤더에 담는다. (contentType, contentLength)
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(S3_fileName)); // 파일 타입 지정
        httpHeaders.setContentLength(bytes.length); // 파일 크기 지정

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

    }


    public String getRandomFileName() {

        Random generator = new java.util.Random();
        generator.setSeed(System.currentTimeMillis());
        String randomNumber = String.format("%06d", generator.nextInt(1000000) % 1000000);

        return convertDateToString(LocalDateTime.now()) + '_' + randomNumber + ".jpg";
    }

    private String convertDateToString(LocalDateTime nowDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return nowDate.format(formatter);
    }

    public ResponseEntity<byte[]> downloadImg(String fileName) throws IOException {

        // download 받을거면 DB에서 original_fileName으로 S3_fileName을 조회하면 된다.

        Member member = memberRepository.findByEmail(getEmail())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        // test Code
        String S3_fileName = member.getImg();


        // S3 해당 bucket에서 해당 이름으로 저장된 이미지를 가져옴.
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, S3_fileName));

        // 파일에 대한 정보를 헤더에 담는다. (contentType, contentLength)
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(S3_fileName)); // 파일 타입 지정
        httpHeaders.setContentLength(bytes.length); // 파일 크기 지정

        // fileName 공백 인코딩
        String downloadName = URLEncoder.encode(S3_fileName, "UTF-8").replaceAll("\\+", "%20");

        // 브라우저에서 실행하는 것이 아니라 파일을 첨부파일로 다운로드하라는 명령
        httpHeaders.setContentDispositionFormData("attachment", downloadName); // 파일이름 지정

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

    }

    private MediaType contentType(String keyname) {
        String[] arr = keyname.split("\\.");
        String type = arr[arr.length - 1];
        switch (type) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    // 사용자 Email 가져오는 Email
    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
