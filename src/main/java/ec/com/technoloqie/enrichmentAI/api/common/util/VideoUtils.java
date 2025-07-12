package ec.com.technoloqie.enrichmentAI.api.common.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ec.com.technoloqie.enrichmentAI.api.common.exception.EnrichmentAIException;
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
}
