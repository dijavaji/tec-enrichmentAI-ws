package ec.com.technoloqie.enrichmentAI.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
class TecEnrichmentAiWsApplicationTests {

	@Test
	void saveFramesAsImages() {
		
		LocalDateTime localDate = LocalDateTime.now();
		
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		
		String videoFilePath = "/home/diego/Videos/enrichmentVisionAi/backtest.mp4"; // Cambia esto a la ruta de tu video
        String outputDir = "/home/diego/Videos/enrichmentVisionAi/output/1";
		
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFilePath);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        
        try {
            grabber.start();
            Frame frame;
            int frameNumber = 0;

            while ((frame = grabber.grabImage()) != null) {
                BufferedImage bufferedImage = converter.convert(frame);
                File outputfile = new File(outputDir + "frame_" + String.format("%04d", frameNumber) + ".png");
                ImageIO.write(bufferedImage, "png", outputfile);
                frameNumber++;
            }

            grabber.stop();
            System.out.println("Conversión completada. Se generaron " + frameNumber + " fotogramas.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                grabber.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
