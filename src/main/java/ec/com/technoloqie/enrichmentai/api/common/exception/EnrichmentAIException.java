package ec.com.technoloqie.enrichmentai.api.common.exception;

public class EnrichmentAIException extends RuntimeException{
	
	public EnrichmentAIException() {
        super();
    }
    
	public EnrichmentAIException (String msg, Throwable nested) {
        super(msg, nested);
    }
    
	public EnrichmentAIException (String message) {
        super(message);
    }
    
	public EnrichmentAIException(Throwable nested) {
        super(nested);
	}
	
	private static final long serialVersionUID = 1L;

}