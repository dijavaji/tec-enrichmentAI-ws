package ec.com.technoloqie.enrichmentAI.api;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
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
		try {
            OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("/home/diego/Videos/enrichmentVisionAi/video_2025-07-11_22-45-45.mp4");
            grabber.start();

            int frameNumber = 0;
            Frame capturedFrame;
            Mat matFrame = new Mat();

            while ((capturedFrame = grabber.grab()) != null) {
                OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
                matFrame = converter.convertToMat(capturedFrame);

                // Save the frame as a JPEG image
                opencv_imgcodecs.imwrite("/home/diego/Videos/enrichmentVisionAi/output/frame_" + frameNumber + ".jpg", matFrame);
                frameNumber++;
            }

            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
