package ec.com.technoloqie.enrichmentAI.api.repository;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoContentDto implements Serializable{
	
	private static final long serialVersionUID = 1902880711950289735L;

	private String text;
	
	private String senderId;
	
	private String assistantName;
	
	private String model;
	//private URI url;
	private String url;
    private String base64Data;
    private String mimeType;
	

}
