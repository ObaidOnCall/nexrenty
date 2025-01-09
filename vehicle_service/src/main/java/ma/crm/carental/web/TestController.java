package ma.crm.carental.web;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.SnowballObject;
import io.minio.UploadObjectArgs;
import io.minio.UploadSnowballObjectsArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/tests")
@Slf4j
public class TestController {
    
    private final MinioClient minioClient ;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    TestController (
        MinioClient minioClient
    ){
        this.minioClient = minioClient ;
    }



    @PostMapping(value = "/fileUpload-V1" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void upload(
        @RequestPart("files") List<MultipartFile> files
    ) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        
            List<CompletableFuture<Void>> uploadFutures = new ArrayList<>();

            for (MultipartFile file : files) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        InputStream fileInputStream = file.getInputStream();
                        minioClient.putObject(
                                PutObjectArgs.builder()
                                        .bucket("clients")
                                        .object(file.getOriginalFilename())
                                        .stream(fileInputStream, file.getSize(), -1)
                                        .contentType(file.getContentType())
                                        .build());
                        log.info("Uploaded: " + file.getOriginalFilename());
                    } catch (Exception e) {
                        log.error("Error uploading file: " + file.getOriginalFilename());
                        e.printStackTrace();
                    }
                }, executorService);
    
                uploadFutures.add(future);
            }

            // Wait for all uploads to complete
            CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0])).join();
            log.info("All files uploaded successfully");
            
        
    }


    @GetMapping("/presignedUrl/{filename}")
    String getPresignedObjectUrl(
        @PathVariable String filename 
    ) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException  {
        
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket("clients")
                .object(filename)
                .expiry(5, TimeUnit.MINUTES)
                .build()
        ) ;
    }


    @PostMapping(value = "/fileUpload-V2" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFiles(
        @RequestPart("files") List<MultipartFile> files
    ) throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException {
        List<SnowballObject> snowballObjects = new ArrayList<>();

        // Convert each MultipartFile to a SnowballObject and add to the list
        for (MultipartFile file : files) {
            SnowballObject snowballObject = new SnowballObject(
                file.getOriginalFilename(),
                new ByteArrayInputStream(file.getBytes()),
                file.getSize(),
                null // Optionally, you can provide compression methods here
            );
            snowballObjects.add(snowballObject);
        }

        // Upload the files as a single TAR package
        minioClient.uploadSnowballObjects(
            UploadSnowballObjectsArgs.builder()
                .bucket("clients")
                .objects(snowballObjects)
                .build()
        );
        
        log.info("All files uploaded successfully as a single TAR archive.");
    }

    @GetMapping("/listObjects")
    public void listObjects() {

        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket("clients").build());

        results.forEach(
            result -> {
                try {
                    log.info(result.get().objectName());
                } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException
                        | InsufficientDataException | InternalException | InvalidResponseException
                        | NoSuchAlgorithmException | ServerException | XmlParserException | IOException e) {
                    e.printStackTrace();
                }
            }
        );
    }
}
