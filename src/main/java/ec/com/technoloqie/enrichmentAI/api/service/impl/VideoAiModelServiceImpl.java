package ec.com.technoloqie.enrichmentAI.api.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import ec.com.technoloqie.enrichmentAI.api.common.exception.EnrichmentAIException;
import ec.com.technoloqie.enrichmentAI.api.common.util.VideoUtils;
import ec.com.technoloqie.enrichmentAI.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentAI.api.repository.VideoContentDto;
import ec.com.technoloqie.enrichmentAI.api.service.IImageAiModelService;
import ec.com.technoloqie.enrichmentAI.api.service.IVideoAiModelService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoAiModelServiceImpl implements IVideoAiModelService{

	//private static final String OUTPUT_FILE_PATH="/var/opt/apptmp/";
	@Value("${ec.com.technoloqie.enrichment.ai.output.file.path}")
    private String outputFilePath;
	
	@Value("classpath:/prompts/prompt-template-theft.st")
	private Resource promptTemplateResource;
	
	private IImageAiModelService imageAiService;
	
	public VideoAiModelServiceImpl(IImageAiModelService imageAiService) {
		this.imageAiService = imageAiService;
	}
	
	@Override
	public ChatDto generateAnalysis(VideoContentDto videoDto) throws EnrichmentAIException{
		
		ChatDto chatDto = null;
		
		if (videoDto == null || StringUtils.isBlank(videoDto.getUrl())) {
			log.error("No video response returned for request: {}", videoDto);
			throw new  EnrichmentAIException("No video response returned for request");
		}
		//Assert.notNull(openAiImageApi, "OpenAiImageApi must not be null");
		
		try {
			VideoUtils.getInstance().downloadVideo(videoDto.getUrl(), outputFilePath + videoDto.getSenderId() + ".mp4");
			
			VideoUtils.getInstance().saveFramesAsImages(outputFilePath + videoDto.getSenderId() + ".mp4",outputFilePath+"/frames/");
			
			Collection<String> colEncoded = new ArrayList<String>();
			//"Analiza el conjunto de imagenes"
			for (int i = 0; i < 5; i++) {
				File file = new File(outputFilePath+"/frames/"+"frame_" + String.format("%04d", i) + ".png");
				String encoded = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
				colEncoded.add(encoded);
			}
			
			SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(promptTemplateResource);
			Message systemMessage = systemPromptTemplate.createMessage(Map.of("frame_number", videoDto.getSenderId()));

			Prompt prompt = new Prompt(List.of(systemMessage));
			
			chatDto = this.imageAiService.getModelResponse(prompt.getContents(), videoDto.getText(), colEncoded, "image/png");
			chatDto.setSenderId(videoDto.getSenderId());
			return chatDto;
			
		}catch(Exception e) {
			log.error("Error al momento de analizar video {}", e);
			throw new  EnrichmentAIException("Error al momento de analizar video",e);
		}
		//return chatDto;
	}

}
