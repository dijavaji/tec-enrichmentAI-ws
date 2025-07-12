package ec.com.technoloqie.enrichmentAI.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ec.com.technoloqie.enrichmentAI.api.common.exception.EnrichmentAIException;
import ec.com.technoloqie.enrichmentAI.api.common.util.VideoUtils;
import ec.com.technoloqie.enrichmentAI.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentAI.api.repository.VideoContentDto;
import ec.com.technoloqie.enrichmentAI.api.service.IVideoAiModelService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoAiModelServiceImpl implements IVideoAiModelService{

	private static final String OUTPUT_FILE_PATH="/var/opt/apptmp/";
	
	@Override
	public ChatDto generateAnalysis(VideoContentDto videoDto) {
		
		if (videoDto == null || StringUtils.isBlank(videoDto.getUrl())) {
			log.error("No video response returned for request: {}", videoDto);
			throw new  EnrichmentAIException("No video response returned for request");
		}
		//Assert.notNull(openAiImageApi, "OpenAiImageApi must not be null");
		
		VideoUtils.getInstance().downloadVideo(videoDto.getUrl(), OUTPUT_FILE_PATH + videoDto.getSenderId() + ".mp4");
		
		return ChatDto.builder().build();
	}

}
