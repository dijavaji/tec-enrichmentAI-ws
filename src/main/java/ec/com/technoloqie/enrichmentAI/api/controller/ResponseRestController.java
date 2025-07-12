package ec.com.technoloqie.enrichmentAI.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ec.com.technoloqie.enrichmentAI.api.common.exception.EnrichmentAIException;
import ec.com.technoloqie.enrichmentAI.api.repository.ChatDto;
import ec.com.technoloqie.enrichmentAI.api.repository.VideoContentDto;
import ec.com.technoloqie.enrichmentAI.api.service.IVideoAiModelService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dvasquez
 */
@CrossOrigin(origins = {"/**"})
@RestController
@RequestMapping("/api/v1/responses")
@Slf4j
public class ResponseRestController {
		
	private IVideoAiModelService videoService;
	
	@Value("${spring.application.name}")
	private String appName;
	
	public ResponseRestController(IVideoAiModelService videoService) {
		this.videoService = videoService;
	}
	
	@GetMapping
    public String getMessage() {
        return String.format("Now this finally works out. Welcome %s",appName);
    }
	
	@PostMapping("/video")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getVideoAnalysis(@Valid @RequestBody VideoContentDto videoDto, BindingResult result) {
		try {
			ChatDto createUser = this.videoService.generateAnalysis(videoDto);
			return new ResponseEntity<>(createUser, HttpStatus.OK); 
		}catch(EnrichmentAIException e) {
			String response = e.getMessage() +" : " + e;
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			log.error("Error al momento de analizar video. {}",e);
			return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
