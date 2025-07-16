package ec.com.technoloqie.enrichmentAI.api.service;

import ec.com.technoloqie.enrichmentAI.api.common.exception.EnrichmentAIException;
import ec.com.technoloqie.enrichmentAI.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentAI.api.repository.VideoContentDto;

public interface IVideoAiModelService {

	ChatDto generateAnalysis(VideoContentDto videoDto) throws EnrichmentAIException;

}
