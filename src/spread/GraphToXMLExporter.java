package spread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.mxgraph.model.mxCell;

public class GraphToXMLExporter {

	public static boolean export(myGraph graph, File file) throws IOException{
		ArrayList<mxCell> verices = graph.getVertices();
		
		BufferedWriter out = new BufferedWriter(
								new OutputStreamWriter(
									new FileOutputStream(file), "UTF8"));
		
		out.write(beginning);
		
		/**
		 * Saving vertices
		 */
		for(mxCell vertex : verices){
			VertexValue value = (VertexValue) vertex.getValue();
			out.write("\t\t<vertex id=\"" + value.getId() +
						"\" name=\"" + value.getName() +
						"\" surname=\"" + value.getSurname() + 
						"\" immunity=\"" + value.getInitialImmunity() +
						"\"/>\n");
		}
		
		out.write(middle);
		
		/**
		 * Saving edges
		 */
		for(mxCell source : verices)
			for(mxCell target : verices)
				if(graph.isEdgeBetween(source, target)){
					out.write("\t\t<edge from=\"" + ((VertexValue) source.getValue()).getId() +
								"\" to=\"" + ((VertexValue) target.getValue()).getId() +
								"\"/>\n");
				}
		
		out.write(ending);
		out.close();
		return true;
	}
	
	private static String beginning = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<graph>\n\t<vertices>\n";
	private static String middle = "\t</vertices>\n\t<edges>\n";
	private static String ending = "\t</edges>\n</graph>\n";
}
