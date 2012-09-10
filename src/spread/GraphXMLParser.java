package spread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


public class GraphXMLParser {
	
	private myGraph graph;
	private GraphXMLParserListener listener;
	
	public GraphXMLParser(myGraph graph, GraphXMLParserListener listener) {
		this.graph = graph;
		this.listener = listener;
	}
	
	public void parse(String filePath){
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			int lines = countLines(filePath);
			SAXParser parser = factory.newSAXParser();
			parser.parse(new File(filePath), new GraphXMLHandler(graph, listener, lines));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int countLines(String filePath) {
	    try {
		    InputStream is = new BufferedInputStream(new FileInputStream(filePath));
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        while ((readChars = is.read(c)) != -1) {
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n')
	                    ++count;
	            }
	        }
	        is.close();
	        return count;
	    } catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
