package com.example.qrcodegenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Map;

@Component
public class ImageWriter {


    private static final Logger logger = LoggerFactory.getLogger(ImageWriter.class);
    QRCodeWriter writer = new QRCodeWriter();

    public BufferedImage getQrCode(String data, int width, int height, ErrorCorrectionLevel errorCorrectionLevel) {
        try {
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            logger.error("Error generating QR Code: {}", e.getMessage(), e);
            return getDefaultImage();
        }
    }

    private BufferedImage getDefaultImage() {
        //TODO replace this with a better implementation
        return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }
}

