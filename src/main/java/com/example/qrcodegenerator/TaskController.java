package com.example.qrcodegenerator;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class TaskController {

    private final ImageWriter imageWriter;
    private static final int DEFAULT_SIZE = 250;
    private static final String DEFAULT_TYPE = "png";
    private static final String DEFAULT_CORRECTION = "L";


    @Autowired
    public TaskController(ImageWriter imageWriter){
        this.imageWriter = imageWriter;
    }

    @GetMapping("/api/health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<byte[]> getImage(@RequestParam String contents,
                                           @RequestParam (required = false, defaultValue = "" + DEFAULT_SIZE) int size,
                                           @RequestParam (required = false, defaultValue = DEFAULT_CORRECTION) String correction,
                                           @RequestParam (required = false, defaultValue = DEFAULT_TYPE) String type) throws IOException {


        if(contents == null || contents.trim().isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\":\"Contents cannot be null or blank\"}".getBytes());
        }

        if (size < 150 || size > 350) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Image size must be between 150 and 350 pixels\"}".getBytes());
        }

        if(!(correction.toUpperCase().equals("L") || correction.toUpperCase().equals("M") ||
                correction.toUpperCase().equals("Q") || correction.toUpperCase().equals("H"))){
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\":\"Permitted error correction levels are L, M, Q, H\"}".getBytes());
        }

        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.valueOf(correction.toUpperCase());

        if ("png".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type) || "gif".equalsIgnoreCase(type)) {
            BufferedImage bufferedImage = imageWriter.getQrCode(contents, size, size, errorCorrectionLevel);

            try (var baos = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, type, baos);
                byte[] bytes = baos.toByteArray();
                return ResponseEntity
                        .ok()
                        .contentType(getContentType(type))
                        .body(bytes);
            } catch (IOException e) {
                // handle the IOEexception
                e.printStackTrace();
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Only png, jpeg and gif image types are supported\"}".getBytes());
        }

    }

    private MediaType getContentType(String type) {
        switch (type.toLowerCase()) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                throw new IllegalArgumentException("Unsupported image type: " + type);
        }
    }
}


