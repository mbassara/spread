package spread;

public interface GraphXMLParserListener {

	public void parserStateChanged(ParserStateChange change);
	
	public void taskStarted(ParserStateChange change);
	
	public void taskFinished(Object source);
	
}
