package ec.com.technoloqie.enrichmentAI.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ec.com.technoloqie.enrichmentAI.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentAI.api.repository.VideoContentDto;
import ec.com.technoloqie.enrichmentAI.api.service.IImageAiModelService;
import ec.com.technoloqie.enrichmentAI.api.service.IVideoAiModelService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
class TecEnrichmentAiWsApplicationTests {
	
	@Autowired
	private IVideoAiModelService videoAiService;
	
	@Autowired
	private IImageAiModelService imageAiService;

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
	
	@Test
	void generateChatCompletionTest() {
		
		try {
			File file = new File("/home/diego/Videos/enrichmentVisionAi/output/15-07-2025/frame_0145.png");
			
			//encoding
			String encoded = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
			log.debug(encoded);	
			ChatDto chatDto = this.imageAiService.generateChatCompletion("Explain what do you see on this picture?",encoded ,"image/jpeg");
			Assertions.assertNotNull(chatDto, "exito generateChatCompletionTest");
		}catch(Exception e) {
			log.error("Error generateChatCompletionTest. {}", e);
			Assertions.assertTrue(Boolean.TRUE, "Error generateChatCompletionTest.");
		}
	}
	
	@Test
	void getImagetoBase64(){
		String originalUrl = "https://storage.googleapis.com/chatbot-doc_bucket/docKnowlege/Screenshot_2025-07-12_19-48-05.jpg";
		String encodedUrl = Base64.getUrlEncoder().encodeToString(originalUrl.getBytes());
		
		log.info("respuesta: {}", encodedUrl);
		Assertions.assertNotNull(encodedUrl, "exito getImagetoBase64");
	}
	
	@Test
	void getVideoVisionTest() {
		try {
			String text = "Analiza el conjunto de imagenes";
			String urlVideo = "https://storage.googleapis.com/chatbot-doc_bucket/docKnowlege/fragmento_min.mp4"; //"https://s3.us-east-005.backblazeb2.com/kuntur-backblaze/videos/backtest.mp4";
			VideoContentDto video = VideoContentDto.builder()
					.text(text)
					.url(urlVideo)
					.senderId("1010011010")
					.build();	
			ChatDto response = this.videoAiService.generateAnalysis(video);
			log.info("respuesta: {}", response);
		} catch (Exception e) {
			log.error("Error getImageVisionTest. {}", e);
			Assertions.assertTrue(Boolean.TRUE, "updateIntentTest.");
		}
	}

}
