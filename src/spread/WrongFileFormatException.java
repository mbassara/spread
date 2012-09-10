package spread;

@SuppressWarnings("serial")
public class WrongFileFormatException extends Exception {
	@Override
	public String getMessage() {
		return "Wrong file format for this operation";
	}
}
