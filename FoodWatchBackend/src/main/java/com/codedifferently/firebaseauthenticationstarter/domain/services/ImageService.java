package com.codedifferently.firebaseauthenticationstarter.domain.services;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import com.codedifferently.firebaseauthenticationstarter.domain.controllers.UserController;
import com.codedifferently.firebaseauthenticationstarter.domain.models.Food;
import com.codedifferently.firebaseauthenticationstarter.domain.repos.FoodRepo;
import com.codedifferently.firebaseauthenticationstarter.security.models.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ImageService {
    @Autowired
    FoodRepo foodRepo;

    private String TEMP_URL;
    private String bucketName = "completefirebasedemo-a06f0.appspot.com";
    public ImageService() {
    }
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    public void saveImage(MultipartFile imageFile) throws IOException {
        String folder = "/foodWatchImages";
        byte[] bytes = imageFile.getBytes();
        Path path = Paths.get(folder + imageFile.getOriginalFilename());
        Files.write(path, bytes);
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("firebase_config.json").getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format("src/main/resources/firebase_config.json", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
    private String deleteFile(String fileName) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("firebase_config.json").getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        storage.delete(blobId);
        return "done";
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public Object upload(MultipartFile multipartFile,String fileName, int calorie) {

        try {
            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            //foodRepo.save(new Food(fileName,calorie));
            file.delete();// to delete the copy of uploaded file stored in the project folder
            return new ResponseEntity<>("Successfully Uploaded !\n"+ TEMP_URL, HttpStatus.ACCEPTED);                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("500"+ e+"Unsuccessfully Uploaded!", HttpStatus.NOT_ACCEPTABLE);                     // Your customized response
        }

    }

//    public Object download(String fileName) throws IOException {
//        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));     // to set random strinh for destination file name
//        String destFilePath = "Z:\\New folder\\" + destFileName;                                    // to set destination file path
//
//        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
//        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("firebase_config.json").getInputStream());
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//        Blob blob = storage.get(BlobId.of(bucketName, fileName));
//        blob.downloadTo(Paths.get(destFilePath));
//        return new ResponseEntity<String>("200 "+ " Successfully Downloaded!", HttpStatus.OK);                     // Your customized response
//    }

    public BufferedImage scaleDown(BufferedImage sourceImage, int targetWidth, int targetHeight) {

        int sourceWidth  = sourceImage.getWidth();
        int sourceHeight = sourceImage.getHeight();
        BufferedImage targetImage = new BufferedImage(targetWidth, targetHeight, sourceImage.getType());
        Graphics2D g = targetImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(sourceImage, 0, 0, targetWidth, targetHeight, 0, 0, sourceWidth, sourceHeight, null);
        g.dispose();
        return targetImage;
    }

    public File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        String var10002 = System.getProperty("java.io.tmpdir");
        File convFile = new File(var10002 + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    public BufferedImage convertImage(BufferedImage image,String fileType) throws IOException{

        if(Objects.equals(fileType,"jpeg")){
            return image;
  }

        final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

            final FileOutputStream fileOutputStream = new FileOutputStream("dice-test.jpeg");
            final boolean canWrite = ImageIO.write(convertedImage, "jpeg", fileOutputStream);
            fileOutputStream.close(); // ImageIO.write does not close the output stream
            if (!canWrite) {
                throw new IllegalStateException("Failed to write image.");
            }
        logger.info("file size = {} x {}, type = {}",image.getWidth(), image.getHeight(), fileType);

        return  convertedImage ;
    }
    public MultipartFile imageToFile(BufferedImage image,String fileName) throws IOException {
        //ByteArrayOutputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //BufferedImage ByteArrayOutputStream
        ImageIO.write(image, "jpeg", os);
        //ByteArrayOutputStream InputStream
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        //InputStream MultipartFile
        MultipartFile file =new MockMultipartFile(fileName, fileName, "text/plain", input);
        return file;
    }
    private byte[] downloadPicture(String url){

        URL urlConnection = null;
        HttpURLConnection httpURLConnection = null;
        try {

            urlConnection = new URL(url);
            httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
            InputStream in = httpURLConnection.getInputStream();
// Use available() Method to get the data length
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            return data;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            httpURLConnection.disconnect();
        }
        return null;
    }
}
