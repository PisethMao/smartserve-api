package co.istad.smartserve.features.qrcodes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Adapter Pattern: Stub Implementation of QR generator.
 * This implementation generates a simple placeholder image instead of a real QR code
 * to remove the dependency on ZXing while maintaining system functionality.
 * In a real-world scenario, this would be replaced by another QR library adapter.
 */
@Component
@Slf4j
public class StubQrGeneratorAdapter implements QrGeneratorAdapter {

    @Override
    public byte[] generateQrCodeImage(String text, int width, int height) throws IOException {
        log.info("Generating placeholder QR code for text: {}", text);
        
        // Create a simple placeholder image with the text
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // QR-like pattern (simple border and text)
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(10, 10, width - 20, height - 20);
        g2d.drawRect(30, 30, width - 60, height - 60);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("QR PLACEHOLDER", 20, height / 2);
        
        g2d.dispose();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        
        return baos.toByteArray();
    }
}
