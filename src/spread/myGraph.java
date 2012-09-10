package spread;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxAnimation;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

public class myGraph {
	private mxGraph graph;
	private mxGraphComponent component;
	private mxGraphLayout layout;
	private int vertexWidth;
	private int vertexHeight;
//	private myCostFunction costFunction;
	private mxAnimation animation;
	private Boolean animationIsPaused;
	private Logger log;

	public myGraph() {
		graph = new mxGraph(); // graf "biblioteczny" na którym działamy
		component = new mxGraphComponent(graph); // component użyty w GUI,
													// zwracamy go przez
													// getComponent()
		layout = new mxHierarchicalLayout(graph); // layout dla grafu
		component.setEnabled(false); // defaultowo można klikać w wierzchołki i
										// krawędzie w GUI zaznaczając je, tutaj
										// blokuje wszystko
//		 component.zoomTo(1, true); // początkowy zoom
		vertexWidth = 120; // wymiary prostokąta
		vertexHeight = 30;
//		costFunction = new myCostFunction(); // funkcja potrzebna przy
//												// findShortestPath()
		animation = null; // na razie jest null, jak będzie używana to się do
							// niej dołączy odpowiednią referencję
		animationIsPaused = false; // na początku animacja nie była jeszcze
									// pauzowana
		log = Logger.getLogger(this.getClass().getName());
	}

	public mxCell insertVertex(VertexValue value) {
		graph.getModel().beginUpdate();
		Object parent = graph.getDefaultParent();
		Object inserted;
		try {
			inserted = graph.insertVertex(parent, null, value, 0, 0,
					vertexWidth, vertexHeight);
			layout.execute(parent);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			graph.getModel().endUpdate();
		}
		return (mxCell) inserted;
	}
	
	public Object insertEdge(mxCell v1, mxCell v2){
		graph.getModel().beginUpdate();
//		log.log(Level.INFO, "Inserting edge between " +
//							((VertexValue) v1.getValue()).getId() +
//							" and " +
//							((VertexValue) v2.getValue()).getId());
		Object parent = graph.getDefaultParent();
		mxCell inserted;
		try
		{
			inserted = (mxCell) graph.insertEdge(parent, null, null, v1, v2);
//			log.log(Level.INFO, "Edge inserted");
			EdgeValue value = new EdgeValue(((VertexValue) v2.getValue()).getInitialImmunity());	// na razie waga krawędzi to po prostu odporność wierzchołka do którego ta krawędź prowadzi
			((VertexValue) v1.getValue()).addNeighbour(v2);		// dodajemy v2 jako sąsiednią krawędź v1
//			log.log(Level.INFO, "Neighbour added to target vertex");
			inserted.setValue(value);		// ustawiamy wartość dodanej krawędzi
//			log.log(Level.INFO, "Edge value set");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			graph.getModel().endUpdate();
		}
		return inserted;
	}

	public mxCell findVertex(String id) {
		Object[] cells = graph.getChildCells(graph.getDefaultParent());
		String logMessage = "\nLooking for vertex with ID: \"" + id + "\"\n";
		for (int i = 0; i < cells.length; i++) {
			mxCell cell = (mxCell) cells[i];
			if (cell.isVertex()) {
				String cellsId = ((VertexValue) cell.getValue()).getId();
				logMessage += "checking vertex ID: \"" + cellsId + "\"\n";
				if (cellsId.equals(id)) {
					logMessage += "vertex found!\n";
					log.log(Level.INFO, logMessage);
					return cell;
				}
			}
		}
		logMessage += "Can't find vertex for ID: \"" + id + "\"\n";
		log.log(Level.INFO, logMessage);
		return null;
	}

	public ArrayList<mxCell> getVertices() {
		ArrayList<mxCell> result = new ArrayList<mxCell>();
		Object[] cells = graph.getChildCells(graph.getDefaultParent());
		for (int i = 0; i < cells.length; i++) {
			if (((mxCell) cells[i]).isVertex())
				result.add((mxCell) cells[i]);
		}
		return result;
	}

	public boolean isEdgeBetween(mxCell source, mxCell target) {
		boolean DIRECTED = true;
		return graph.getEdgesBetween(source, target, DIRECTED).length != 0;
	}

	public ArrayList<mxCell> getEdges() {
		ArrayList<mxCell> result = new ArrayList<mxCell>();
		Object[] cells = graph.getChildCells(graph.getDefaultParent());
		for (int i = 0; i < cells.length; i++) {
			if (((mxCell) cells[i]).isEdge())
				result.add((mxCell) cells[i]);
		}
		return result;
	}
	
	public mxCell getEdgeBetween(mxCell from, mxCell to, boolean isDirected) {
		return (mxCell) graph.getEdgesBetween(from, to, isDirected)[0];
	}

	public int getSize() { // ilość wszystkich mxCells (krawędzie i wierzchołki)
		return graph.getChildCells(graph.getDefaultParent()).length;
	}

	public BufferedImage getBufferedImage() {
		return mxCellRenderer.createBufferedImage(graph,
				graph.getChildCells(graph.getDefaultParent()), 1.5,
				Color.WHITE, true, null);
	}

	public void clearSelection() {
		Object[] cells = graph.getChildCells(graph.getDefaultParent()); // get
																		// wszystkie
																		// cells
		for (Object cell : cells) {
			((mxCell) cell).setStyle(null); // ustaw defaultowy kolor itp

			if (((mxCell) cell).isVertex())
				((VertexValue) ((mxCell) cell).getValue()).resetValue(); // wierzchołkom
																			// jeszcze
																			// czyścimy
																			// wartości
		}
		animationIsPaused = false;
		graph.refresh();
	}

	public void reset() { // hard reset - ustawiamy nowy model - wszystkie dane
							// z grafu są tracone
		graph.setModel(new mxGraphModel());
	}

	public mxGraphComponent getComponent() {
		return component; // zwracamy komponent do użycia w GUI
	}
	
	public void refresh() {
		graph.refresh();
	}
	
	public void executeLayout() {
		layout.execute(graph.getDefaultParent());
	}
	
	public int findShortestPath(Object from, Object to, int max_steps, StatisticsPane statisticsPane, int delay){
		
		animation = new BFSSimulation((mxCell) from, (mxCell) to, this, statisticsPane, delay);
		animation.startAnimation();
		
		return 1;

	}

	public void stopAnimation() {
		if (animation != null) {
			animationIsPaused = true;
			animation.stopAnimation();
		}
	}

	public int startAnimation() {
		if (animationIsPaused) {
			animation.startAnimation();
			return 0;
		} else {
			return 1;
		}
	}

	
	public mxRectangle getGraphBounds() {
		return graph.getGraphBounds();
	}
}
