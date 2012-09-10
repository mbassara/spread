package spread;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.mxgraph.model.mxCell;



public class GraphXMLHandler extends DefaultHandler2 {
	
	private myGraph graph;
	private Logger log;
	private HashMap<String, mxCell> verticesMap;
	private GraphXMLParserListener listener;
	private int currentProgress;
	private int taskLength;
	private ParserStateChange stateChange;

	public GraphXMLHandler(myGraph graph, GraphXMLParserListener listener, int linesConut){
		super();
		this.graph = graph;
		log = Logger.getLogger(this.getClass().getName());
		verticesMap = new HashMap<String, mxCell>();
		this.listener = listener;
		currentProgress = 0;
		taskLength = linesConut - 8;		// wszystkie linie oprócz 8 zawierają dodawanie krawędzi bądź wierzchołków
		stateChange = new ParserStateChange();
	}
	
	@Override
	public void startDocument() throws SAXException {
		stateChange.setParameters(this, 0, taskLength, "Starting parsing XML file");
		if(listener != null)
			listener.parserStateChanged(stateChange);
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {
		log.log(Level.INFO, "Parsing finished");
		super.endDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(qName.equals("vertex")){
			VertexValue value = new VertexValue();
			log.log(Level.INFO, "Inserting new vertex from XML\n");
			String logMessage = "Parsed attributes:\n";
			String id = null;
			for(int i = 0; i < attributes.getLength(); i++){
				String attrName = attributes.getLocalName(i);
				String attrVal = attributes.getValue(i);
				if(attrName.equals("id")) {
					value.setId(attrVal);
					id = new String(attrVal);
				}
				else if(attrName.equals("name"))
					value.setName(attrVal);
				else if(attrName.equals("surname"))
					value.setSurname(attrVal);
				else if(attrName.equals("immunity"))
					value.setImmunity(Double.parseDouble(attrVal));
				logMessage += attrName + "=\"" + attrVal + "\"\n";
			}
			log.log(Level.INFO, logMessage);
			mxCell inserted = graph.insertVertex(value);
			verticesMap.put(id, inserted);
			stateChange.setParameters(this, ++currentProgress, taskLength, "#" + id + " vertex inserted");
			if(listener != null)
				listener.parserStateChanged(stateChange);
			log.log(Level.INFO, "Inserted vertex:\n" + value.toString() +"\n");
		}
		else if(qName.equals("edge")){
			mxCell v1 = null,v2 = null;
			log.log(Level.INFO, "Inserting new edge from XML\n");
			String logMessage = "Parsed attributes:\n";
			for(int i = 0; i < attributes.getLength(); i++){
				String attrName = attributes.getLocalName(i);
				String attrVal = attributes.getValue(i);
				logMessage += attrName + "=\"" + attrVal + "\"\n";
				log.log(Level.INFO, logMessage);
				if(attrName.equals("from"))
					v1 = verticesMap.get(attrVal);		// szybsza implementacja oparta o hashMapę
//					v1 = graph.findVertex(attrVal);		// wolniejsza implementacja
				else if(attrName.equals("to"))
					v2 = verticesMap.get(attrVal);		// szybsza implementacja oparta o hashMapę
//					v2 = graph.findVertex(attrVal);		// wolniejsza implementacja
			}
			graph.insertEdge(v1, v2);
			logMessage = "Inserted edge:\n" +
						((VertexValue) ((mxCell) v1).getValue()).getId() +
						" --> " +
						((VertexValue) ((mxCell) v2).getValue()).getId() + "\n";
			stateChange.setParameters(this, ++currentProgress, taskLength, logMessage);
			if(listener != null)
				listener.parserStateChanged(stateChange);
			log.log(Level.INFO, logMessage);
		}
	}
}
