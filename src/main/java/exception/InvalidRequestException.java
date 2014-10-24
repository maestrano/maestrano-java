package exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvalidRequestException extends MnoException {
	
	public InvalidRequestException(String message) {
		super(message);
	}
	
	public InvalidRequestException(String message, Throwable e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
