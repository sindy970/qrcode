package org.example.qrcode.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QrController {
    @GetMapping(value = {"", "/"})
    public ResponseEntity<byte[]> qrToNotion() throws WriterException, IOException {
        // QR 정보
        int width = 300;
        int height = 300;
        String url = "http://section.blog.naver.com/connect/PopConnectBuddyAddForm.nhn?blogId=dailysomm";

        // QR Code - BitMatrix: qr code 정보 생성
        BitMatrix encode = new MultiFormatWriter()
                .encode(url, BarcodeFormat.QR_CODE, width, height);

        //(QRcode color, background color)
        MatrixToImageConfig custom = new MatrixToImageConfig(Color.PINK.getRGB(), Color.BLACK.getRGB());

        // QR Code - Image 생성. : 1회성으로 생성해야 하기 때문에
        // stream으로 Generate(1회성이 아니면 File로 작성 가능.)
        try {
            //output Stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //Bitmatrix, file.format, outputStream
            MatrixToImageWriter.writeToStream(encode, "PNG", out, custom);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());

        } catch (Exception e) {
            log.warn("QR Code OutputStream 도중 Excpetion 발생, {}", e.getMessage());
        }

        return null;
    }

    // Image File Response
    @GetMapping("/QRUrl")
    public ResponseEntity<byte[]> inputUrl(@RequestParam String url) throws WriterException {
        // QR 정보
        int width = 300;
        int height = 300;

        // QR Code - BitMatrix: qr code 정보 생성
        BitMatrix encode = new MultiFormatWriter()
                .encode(url, BarcodeFormat.QR_CODE, width, height);

        //(QRcode color, background color)
        MatrixToImageConfig custom = new MatrixToImageConfig(Color.PINK.getRGB(), Color.BLACK.getRGB());

        // QR Code - Image 생성. : 1회성으로 생성해야 하기 때문에
        // stream으로 Generate(1회성이 아니면 File로 작성 가능.)
        try {
            //output Stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //Bitmatrix, file.format, outputStream
            MatrixToImageWriter.writeToStream(encode, "PNG", out, custom);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());

        } catch (Exception e) {
            log.warn("QR Code OutputStream 도중 Excpetion 발생, {}", e.getMessage());
        }
        // Return
        return null;
    }

    // Image to Byte[]
    private byte[] getImageBye(String imageName) throws IOException{
        // image 경로 및 File
        ClassPathResource classPathResource = new ClassPathResource("static/images/"+ imageName);
        File file = classPathResource.getFile();

        byte[] byteImage = null;

        BufferedImage originalImage = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        originalImage = ImageIO.read(file);
        ImageIO.write(originalImage, "png", out);
        out.flush();

        byteImage = out.toByteArray();
        return byteImage;
    }
}
