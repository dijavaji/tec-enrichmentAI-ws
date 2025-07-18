package ec.com.technoloqie.enrichmentai.api.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import ec.com.technoloqie.enrichmentai.api.common.exception.EnrichmentAIException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VideoUtils {
	
	// objeto que sera instanciado
	private static VideoUtils instance = new VideoUtils();

	private VideoUtils() {
	}

	public static VideoUtils getInstance() {
		if (null == instance) {
			instance = new VideoUtils();
		}
		return instance;
	}
	
	public static void downloadVideo(String videoUrl, String outputFilePath) {
        try (InputStream in = new BufferedInputStream(new URL(videoUrl).openStream());
        		
             FileOutputStream out = new FileOutputStream(outputFilePath)) {
             
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
            log.info("Descarga completada: " + outputFilePath);
        } catch (IOException e) {
            log.error("Error al descargar video", e);
        	throw new EnrichmentAIException("Error al descargar video", e);
        }
    }
	
	public static void saveFramesAsImages(String videoFilePath, String outputDir) {
		
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFilePath);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        
        try {
            grabber.start();
            Frame frame;
            int frameNumber = 0;

            while ((frame = grabber.grabImage()) != null) {
                BufferedImage bufferedImage = converter.convert(frame);
                if(Math.floorDiv(frameNumber, 30) == 0) {
                	File outputfile = new File(outputDir + "frame_" + String.format("%04d", frameNumber) + ".png");
                    ImageIO.write(bufferedImage, "png", outputfile);
                }
                frameNumber++;
            }

            grabber.stop();
            log.info("conversion completada. Se generaron " + frameNumber + " fotogramas.");
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
