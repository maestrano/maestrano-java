package exception;

public class MnoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MnoException(String message) {
		super(message, null);
	}

	public MnoException(String message, Throwable e) {
		super(message, e);
	}
}
