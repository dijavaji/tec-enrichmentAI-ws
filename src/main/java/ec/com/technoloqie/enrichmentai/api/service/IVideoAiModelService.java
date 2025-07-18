package ec.com.technoloqie.enrichmentai.api.service;

import ec.com.technoloqie.enrichmentai.api.common.exception.EnrichmentAIException;
import ec.com.technoloqie.enrichmentai.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentai.api.repository.VideoContentDto;

public interface IVideoAiModelService {

	ChatDto generateAnalysis(VideoContentDto videoDto) throws EnrichmentAIException;

}
