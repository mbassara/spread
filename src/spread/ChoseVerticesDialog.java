package spread;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import com.mxgraph.model.mxCell;

import SpringUtilities.SpringUtilities;

public class ChoseVerticesDialog {
	
	public static String RELATIONSHIP = "icons/addrelationshipbig.png";
	public static String FIND_PATH = "icons/infectbig.png";
	
	public static Object[] showDialog(myGraph graph, Component parent, String questionType) {
		if(graph.getVertices().size() == 0){
			JOptionPane.showMessageDialog(parent, "Graph is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		ArrayList<mxCell> vertices = graph.getVertices();
		ArrayList<String> verticesNames = new ArrayList<String>();
		
		for(mxCell vertex : vertices){
			VertexValue value = (VertexValue) vertex.getValue();
			verticesNames.add(value.getName() + " " + value.getSurname());	// wyciągam imiona i nazwiska z wierzchołków grafu
		}
		
		SpringLayout layout = new SpringLayout();
		JPanel panel = new JPanel(layout);
		
		JComboBox fromComboBox = new JComboBox(verticesNames.toArray());
		JComboBox toComboBox = new JComboBox(verticesNames.toArray());
		JSpinner speedSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 101, 1));
		
		panel.add(new JLabel("From: "));
		panel.add(fromComboBox);
		panel.add(new JLabel("To: "));
		panel.add(toComboBox);
		if(questionType == FIND_PATH) {
			panel.add(new JLabel("Simulation speed: "));
			panel.add(speedSpinner);
		}
		
		if(questionType == FIND_PATH)
			SpringUtilities.makeCompactGrid(panel, 
											3, 2,
											5, 5,
											5, 5);
		else
			SpringUtilities.makeCompactGrid(panel, 
											1, 4,
											5, 5,
											5, 5);
		
		int result = JOptionPane.showConfirmDialog(parent, panel, 
					"Chose pair of people", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon(ClassLoader.getSystemClassLoader().getResource(questionType)));
		
		if (result == JOptionPane.OK_OPTION) {
			Object[] chosenCells = new Object[5];
			chosenCells[0] = vertices.get(fromComboBox.getSelectedIndex());
			chosenCells[1] = vertices.get(toComboBox.getSelectedIndex());
			chosenCells[2] = fromComboBox.getSelectedItem();
			chosenCells[3] = toComboBox.getSelectedItem();
			chosenCells[4] = speedSpinner.getValue();
			return chosenCells;
		}
		else {
			return null;
		}
	}
}
