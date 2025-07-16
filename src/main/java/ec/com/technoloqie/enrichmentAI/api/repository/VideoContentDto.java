package ec.com.technoloqie.enrichmentAI.api.repository;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoContentDto implements Serializable{
	
	private static final long serialVersionUID = 1902880711950289735L;
	//@NotEmpty(message ="text should not be null or empty")
	private String text;
	@NotEmpty(message ="senderId should not be null or empty")
	@NotNull
	private String senderId;
	
	private String assistantName;
	
	private String model;
	//private URI url;
	@NotEmpty(message ="url should not be null or empty")
	private String url;
    private String base64Data;
    private String mimeType;
	

}
