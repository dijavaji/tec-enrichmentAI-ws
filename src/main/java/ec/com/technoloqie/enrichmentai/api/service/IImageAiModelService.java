package ec.com.technoloqie.enrichmentai.api.service;

import java.util.Collection;

import ec.com.technoloqie.enrichmentai.api.repository.ChatDto;

public interface IImageAiModelService {
	
	
	String generateChat(String prompt, String url);
	
	ChatDto generateChat(String prompt,String base64Data, String mimeType);

	ChatDto generateChatCompletion(String prompt, String base64Data, String mimeType);

	ChatDto getModelResponse(String prompt, String chat, Collection<String> base64Imgcol, String mimeType);
	

}
