package ec.com.technoloqie.enrichmentai.api.repository;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class ChatDto {
	
	private String senderId;	//< Identificador unico del chat al que enviar el mensaje
	private String response;
	private String createdBy;
	private Date createdDate;
	private Boolean status;
	private String generationId;
	private String assistantName;
}
