package generator;
import generator.GraphGenerator;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import SpringUtilities.SpringUtilities;

public class GeneratorDialog {
	
	private static final int MAX_VERTICES = 700; 
	private static final int MAX_EDGES_FROM_ONE_VERTEX = 50;
	private static final int MAX_EDGES_ON_GRAPH = 3000;
	
	public static String show(Component parent){
		return show(parent, null);
	}
	
	public static String show(Component parent, Icon icon) {
		
		SpringLayout layout = new SpringLayout();
		JPanel panel = new JPanel(layout);
		
		final JSpinner verticesSpinner = new JSpinner(new SpinnerNumberModel(
													2,
													2,
													MAX_VERTICES,
													1));
		final JSpinner edgesSpinner = new JSpinner(new SpinnerNumberModel(
													1,
													1,
													1,
													1));
		
		verticesSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				int currentVerticesValue = (Integer) verticesSpinner.getValue();
				int newMaxEdgesValue;
				
				// gwarancja że currentVertVal > maxEdgesVal
				if(MAX_EDGES_ON_GRAPH / currentVerticesValue >= currentVerticesValue)
					newMaxEdgesValue = currentVerticesValue - 1;
				else {
					newMaxEdgesValue = MAX_EDGES_ON_GRAPH / currentVerticesValue;
				}
				
				if(newMaxEdgesValue > MAX_EDGES_FROM_ONE_VERTEX)
					newMaxEdgesValue = MAX_EDGES_FROM_ONE_VERTEX;
				
				if(((Integer) edgesSpinner.getValue()) > newMaxEdgesValue)
					edgesSpinner.setValue(newMaxEdgesValue);
				
				((SpinnerNumberModel) edgesSpinner.getModel()).setMaximum(newMaxEdgesValue);
			}
		});
		
		JLabel fromLabel = new JLabel("Number of people: ");
		JLabel toLabel = new JLabel("Number of relationships per person: ");
		
		panel.add(fromLabel);
		panel.add(verticesSpinner);
		panel.add(toLabel);
		panel.add(edgesSpinner);
		
		SpringUtilities.makeCompactGrid(panel, 
										2, 2,
										5, 5,
										5, 5);
		
		int result = JOptionPane.showConfirmDialog(parent, panel, 
					"Generate society", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					icon);
		
		if (result == JOptionPane.OK_OPTION) {
			JFileChooser chooser = new JFileChooser();
			int chooserResult = chooser.showSaveDialog(parent);
			if(chooserResult == JFileChooser.APPROVE_OPTION){
				File selectedFile = chooser.getSelectedFile();
				
				return GraphGenerator.generate(selectedFile,
										(Integer) verticesSpinner.getValue(),
										(Integer) edgesSpinner.getValue());
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		GeneratorDialog.show(null);
	}
}
