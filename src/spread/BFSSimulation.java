package spread;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxAnimation;

/**
 * 
 * Animacja przedstawiająca rozprzestrzenianie się infekcji
 * opiera się na lekko zmodyfikowanym algorytmie przeszukiwania wszerz
 * implementacja dostosowana do "krokowego" przedstawienia przy pomocy klasy mxAnimation
 * @author maciekb
 *
 */
public class BFSSimulation extends mxAnimation{
	
	private myGraph graph;
	private mxCell from;
	private mxCell to;
	private LinkedList<mxCell> infectionList;			// lista osób które w danym kroku są zarażane
	private Logger log;
	private final int MIN_DELAY = 1;
	private final int MAX_DELAY = 2000;
	
	private StatisticsPane statisticsPane;
	
	private int healthyCounter = 0;
	private int inProgressCounter = 0;
	private int infectedCounter = 0;
	
	public BFSSimulation(mxCell from, mxCell to, myGraph graph, StatisticsPane statisticsPane, int percentageSpeed) {
		int delay;
		if(percentageSpeed == 101)
			delay = 0;
		else
			delay = (int) (MAX_DELAY - (MAX_DELAY - MIN_DELAY) * percentageSpeed / 100.0);
		super.setDelay(delay);
		this.from = from;
		this.to = to;
		this.graph = graph;
		this.infectionList = new LinkedList<mxCell>();
		log = Logger.getLogger(BFSSimulation.class.getName());
		((VertexValue) from.getValue()).setDistance(0);
		((VertexValue) from.getValue()).setInfector(null);		// pierwszy wierzchołek nie ma ojca
		
		this.statisticsPane = statisticsPane;
		healthyCounter = graph.getVertices().size();
		
		infectionList.offer(from);
		healthyCounter--;
		inProgressCounter++;
		log.log(Level.INFO, "Simulation started");
	}
	
	public void updateAnimation(){
		
		statisticsPane.setHealthy(healthyCounter);
		statisticsPane.setInProgress(inProgressCounter);
		statisticsPane.setInfected(infectedCounter);
		
		if(((VertexValue) to.getValue()).isInfected()){
			log.log(Level.INFO, "target is infected, stopAnimation()");
			this.stopAnimation();
			this.finish(to);				// jeśli target jest zarażony to koniec
			return;
		}
		if(infectionList.isEmpty()){
			log.log(Level.INFO, "List is empty, stopAnimation()");
			this.stopAnimation();			// jeśli wszyscy osiągalni są zarażeni to koniec zabawy
			statisticsPane.appendMessage(String.format("%s %s cannot infect %s %s",
														((VertexValue) from.getValue()).getName(),
														((VertexValue) from.getValue()).getSurname(),
														((VertexValue) to.getValue()).getName(),
														((VertexValue) to.getValue()).getSurname()));
			return;
		}

		/**
		 * Zadajemy obrażenia wszystkim którzy są narażeni na infekcję
		 */
		mxCell parent;
		for(mxCell vertex : infectionList){
			((VertexValue) vertex.getValue()).reduceImmunity(4.0);
			double healthRatio = ((VertexValue) vertex.getValue()).getHealthRatio();
			String color = BFSSimulation.generateHexColor(healthRatio);
			vertex.setStyle("fillColor=" + color);
			
			parent = ((VertexValue) vertex.getValue()).getInfector();
			if(parent != null){
				mxCell edge = graph.getEdgeBetween(parent, vertex, true);	// wyciągam krawędź z pomiędzy zarażającego i zarażonego
				edge.setStyle("strokeWidth=2;strokeColor=" + color);
			}
			
			graph.refresh();
		}
		
		LinkedList<mxCell> tmpList = new LinkedList<mxCell>();
		for(mxCell vertex : infectionList)
			tmpList.offer(vertex);			// kopiujmy zawartość infectionList do tymczasowej listy
											// bo interując po elementach infectionList chcemy ją
											// jednocześnie aktualizować więc będziemy iterować po tmpList
		for(mxCell vertex : tmpList){
			if(((VertexValue) vertex.getValue()).isInfected()){
				infectedCounter++;				// zarażony - inkrementacja chorych
				inProgressCounter--;
				infectionList.remove(vertex);	// jeśli jest zarażony to usuwamy z listy
				statisticsPane.appendMessage("\n");
				statisticsPane.appendMessage(String.format("%s %s is sick, now he can infect his neighbours",
														((VertexValue) vertex.getValue()).getName(),
														((VertexValue) vertex.getValue()).getSurname()));
				
				for(mxCell neighbour : ((VertexValue) vertex.getValue()).getNeighbours())		// narażeni na infekcje są wszyscy niezarazeni sąsiedzi
					if(!infectionList.contains(neighbour) && !((VertexValue) neighbour.getValue()).isInfected()){
						((VertexValue) neighbour.getValue()).setInfector(vertex);				// ustawiamy sąsiadowi kto go zaraził
						infectionList.offer(neighbour);
						statisticsPane.appendMessage(String.format("%s %s - infection is in progress",
																((VertexValue) neighbour.getValue()).getName(),
																((VertexValue) neighbour.getValue()).getSurname()));
						healthyCounter--;
						inProgressCounter++;
					}
			}
		}
	}
	
	private void finish(mxCell lastVertex){		// końcowe zaznaczenie ścieżki od from do to
		log.log(Level.INFO, "Marking shortest path");
		mxCell current = lastVertex;
		mxCell parent;
		int length = 0;
		while(true){
			log.log(Level.INFO, "Marking vertex: " + ((VertexValue) current.getValue()).getId());
			parent = ((VertexValue) current.getValue()).getInfector();
			current.setStyle("fillColor=#000000");	// A80000
			if(parent != null){
				mxCell edge = graph.getEdgeBetween(parent, current, true);	// wyciągam krawędź z pomiędzy zarażającego i zarażonego
				edge.setStyle("strokeWidth=3;strokeColor=#000000");
			}
			
			if(parent != null)
				current = parent;
			else
				break;
			length++;
		}
		graph.refresh();
		this.stopAnimation();
		statisticsPane.appendMessage("\n");
		statisticsPane.appendMessage(String.format("%s %s can infect %s %s in %d steps.",
												((VertexValue) from.getValue()).getName(),
												((VertexValue) from.getValue()).getSurname(),
												((VertexValue) to.getValue()).getName(),
												((VertexValue) to.getValue()).getSurname(),
												length));
	}
	
	private static String generateHexColor(double ratio){			// od zielonego do czerwonego (#00FF00 --> #FF0000)
		
		String redByte		= Integer.toHexString(0xFF - (int) (0xFF * 2 * ((ratio > 0.5) ? ratio - 0.5 : 0.0)));	// od 0.0 do 0.5 zielony jest na full i dodajemy czerwonego
		String greenByte	= Integer.toHexString((int) (0xFF * 2 * ((ratio > 0.5) ? 0.5 : ratio)));	// od 0.5 do 1.0 czerwony jest na full i odejmujemy zielonego
		redByte = (redByte.length() == 1) ? "00" : redByte;
		greenByte = (greenByte.length() == 1) ? "00" : greenByte;		// bo toHexString zero zapisuje jako "0" a my chcemy "00"
		return "#" + redByte + greenByte + "00";
	}
}
