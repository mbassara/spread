package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public abstract class GraphGenerator {
	
	public static String generate(File file, int numberOfVertices, int maxNumberOfEgdesConnectedToOneVertex) {
		
		try {
			Random random = new Random();
			BufferedWriter out = new BufferedWriter(
									new OutputStreamWriter(
										new FileOutputStream(file),
										"UTF8"));
										
			
			out.write(beginning);
			
			for(int i = 0; i < numberOfVertices; i++) {
				int id = i;
				String name = String.valueOf(id);						// na razie uproszczony generator:
				String surname = String.valueOf(id);					// imie i nazwistko to ID
				int immunity = random.nextInt(100) + 1;	// immunity: [1,100]
				
				out.write(String.format(Locale.US,
										"\t\t<vertex id=\"%d\" name=\"%s\" surname=\"%s\" immunity=\"%.2f\"/>%n",
										id, name, surname, (double) immunity));
			}
			
			out.write(middle);
			
			Set<Integer> alreadyConnected = new HashSet<Integer>();
			for(int i = 0; i < numberOfVertices; i++) {
				alreadyConnected.clear();
				int edges = random.nextInt(maxNumberOfEgdesConnectedToOneVertex) + 1;
				for(int j = 0; j < edges; j++) {
					int targetVertexID = random.nextInt(numberOfVertices);
					while(targetVertexID == i || alreadyConnected.contains(targetVertexID))
						targetVertexID = random.nextInt(numberOfVertices);	// żeby nie było self-loop'ów
					
					alreadyConnected.add(targetVertexID);
					out.write(String.format("\t\t<edge from=\"%d\" to=\"%d\"/>%n",
											i, targetVertexID));
				}
			}
			
			out.write(ending);
			out.close();

			return file.getCanonicalPath();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String beginning = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<graph>\n\t<vertices>\n";
	private static String middle = "\t</vertices>\n\t<edges>\n";
	private static String ending = "\t</edges>\n</graph>\n";
	
	public static void main(String[] args) {

		generate(new File("src/data/generated.xml"), 100, 3);

	}
}
