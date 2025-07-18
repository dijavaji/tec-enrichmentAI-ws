package ec.com.technoloqie.enrichmentai.api.service.impl;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;
import ec.com.technoloqie.enrichmentai.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentai.api.service.IImageAiModelService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageAiModelServiceImpl implements IImageAiModelService{

	//private static final String BASE_URL = "http://localhost:11434";
	@Value("${ec.com.technoloqie.enrichment.ai.ollama.base-url}")
    private String baseUrl;

	@Override
	public String generateChat(String chat, String url) {
		ChatLanguageModel model = OllamaChatModel.builder().baseUrl(baseUrl).modelName("gemma3:27b") // ("qwen2:0.5b") // ("deepseek-r1:14b")
				.temperature(0.1)
				// .timeout(Duration.ofSeconds(60))
				.build();

		dev.langchain4j.data.message.UserMessage userMessage = dev.langchain4j.data.message.UserMessage.from(
				dev.langchain4j.data.message.TextContent.from(chat),
				dev.langchain4j.data.message.ImageContent.from(url));
		Response<AiMessage> response = model.generate(userMessage);

		return response.content().text();
	}

	@Override
	public ChatDto generateChat(String chat, String base64Data, String mimeType) {
		
		ChatLanguageModel model = OllamaChatModel.builder().baseUrl(baseUrl).modelName("gemma3:27b") // ("qwen2:0.5b") // ("deepseek-r1:14b")
				.temperature(0.1)
				// .timeout(Duration.ofSeconds(60))
				.build();

		dev.langchain4j.data.message.UserMessage userMessage = dev.langchain4j.data.message.UserMessage.from(
				dev.langchain4j.data.message.TextContent.from(chat),
				dev.langchain4j.data.message.ImageContent.from(base64Data, mimeType));
		Response<AiMessage> response = model.generate(userMessage);

		return ChatDto.builder()
				.response(response.content().text())
				.build();
	}	
	
	@Override
	public ChatDto generateChatCompletion(String prompt, String base64Data, String mimeType) {
		ChatLanguageModel model = OllamaChatModel.builder().baseUrl(baseUrl).modelName("gemma3:27b") // ("qwen2:0.5b") // ("deepseek-r1:14b")
				.temperature(0.1)
				// .timeout(Duration.ofSeconds(60))
				.build();
		
		SystemMessage systemMessage = systemMessage(prompt);
		dev.langchain4j.data.message.UserMessage userMessage = dev.langchain4j.data.message.UserMessage.from(
				dev.langchain4j.data.message.TextContent.from("determina si es un robo o no"),
				dev.langchain4j.data.message.ImageContent.from(base64Data, mimeType));
		AiMessage aiMessage = model.generate(systemMessage,userMessage).content();
        return ChatDto.builder()
				.response(aiMessage.text())
				.build();
	}
	
	@Override
	public ChatDto getModelResponse(String prompt, String chat,Collection<String> base64Imgcol, String mimeType) {

		ChatLanguageModel model = OllamaChatModel.builder().baseUrl(baseUrl).modelName("gemma3:27b") // ("qwen2:0.5b") // ("deepseek-r1:14b")
				.temperature(0.1)
				// .timeout(Duration.ofSeconds(60))
				.build();
		
		List<Content> imageContentlst = new ArrayList<>();
		for (String base64Img : base64Imgcol) {
			ImageContent imageContent = dev.langchain4j.data.message.ImageContent.from(base64Img, mimeType);
			imageContentlst.add(imageContent);
		}

		imageContentlst.add(dev.langchain4j.data.message.TextContent.from(chat));

		dev.langchain4j.data.message.UserMessage userMessage = dev.langchain4j.data.message.UserMessage
				.from(imageContentlst);

		SystemMessage systemMessage = systemMessage(prompt);
		Response<AiMessage> response = model.generate(userMessage, systemMessage);
		
		log.info("respuesta ai {} {}", response.tokenUsage(), response.metadata());
		
		return ChatDto.builder()
				.response(response.content().text())
				.createdBy("app-be")
				.status(Boolean.TRUE)
				.createdDate(new Date())
				.generationId(String.valueOf(response.hashCode()))
				.build();
	}
	
}
