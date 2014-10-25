package exception;

public class InvalidRequestException extends MnoException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidRequestException(String message) {
		super(message);
	}
	
	public InvalidRequestException(String message, Throwable e) {
		super(message, e);
	}
}
