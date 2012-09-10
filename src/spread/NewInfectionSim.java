package spread;

import generator.GraphGenerator;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;

import com.mxgraph.util.mxPoint;

import generator.*;

@SuppressWarnings("serial")
public class NewInfectionSim extends JFrame implements ActionListener,
		ChangeListener {

	private NewInfectionSim frame;
	private JPanel contentPane;
	private myGraph graph;
	private JMenuItem mntmOpenGraph;
	private JMenuItem mntmSaveGraph;
	private JMenuItem mntmGenerateGraph;
	private JMenuItem mntmToPng;
	private JMenuItem mntmToJpg;
	private JMenuItem mntmToGif;
	private JMenuItem mntmToBmp;
	private JButton btnSaveGraph;
	private JButton btnOpenGraph;
	private JTabbedPane tabbedPane;
	private JPanel viewPanel;
	private JPanel editPanel;
	private JPanel analysePanel;
	private StatisticsPane statisticsPane;
	
	private boolean isGraphicalModeActive = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewInfectionSim frame = new NewInfectionSim();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewInfectionSim() {

		frame = this;
		graph = new myGraph();

		setTitle("Infection Simulator ver. 0.3a");
		setIconImage((new ImageIcon(ClassLoader.getSystemClassLoader()
				.getResource("icons/programsmall.png"))).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 450, 300);
		setBounds(300, 300, 1024, 700);
		// setPreferredSize(new java.awt.Dimension(600, 600));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmOpenGraph = new JMenuItem("Open graph");
		mntmOpenGraph.addActionListener(this);
		mnFile.add(mntmOpenGraph);
		mntmOpenGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));

		mntmSaveGraph = new JMenuItem("Save graph");
		mntmSaveGraph.addActionListener(this);
		mnFile.add(mntmSaveGraph);
		mntmSaveGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		
		mntmGenerateGraph = new JMenuItem("Generate graph");
		mntmGenerateGraph.addActionListener(this);
		mnFile.add(mntmGenerateGraph);
		mntmGenerateGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));

		JMenu mnHell = new JMenu("Export");
		mnFile.add(mnHell);

		mntmToPng = new JMenuItem("to PNG");
		mnHell.add(mntmToPng);
		mntmToPng.addActionListener(this);

		mntmToJpg = new JMenuItem("to JPG");
		mnHell.add(mntmToJpg);
		mntmToJpg.addActionListener(this);

		mntmToGif = new JMenuItem("to GIF");
		mnHell.add(mntmToGif);
		mntmToGif.addActionListener(this);

		mntmToBmp = new JMenuItem("to BMP");
		mnHell.add(mntmToBmp);
		mntmToBmp.addActionListener(this);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								frame,
								"Infection Simulator ver. 0.2a\nAuthors:\nPrzemysław Sokół\nMaciej Bassara",
								"About",
								JOptionPane.INFORMATION_MESSAGE,
								new ImageIcon(ClassLoader
										.getSystemClassLoader().getResource(
												"icons/programbig.png")));
			}
		});
		mnAbout.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		viewPanel = new JPanel(new BorderLayout(0, 0));
		tabbedPane.addTab("View", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/view.png")),
				viewPanel);

		editPanel = new JPanel(new BorderLayout(0, 0));
		tabbedPane.addTab("Edit", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/edit.png")),
				editPanel);
		tabbedPane.setSelectedComponent(editPanel);

		analysePanel = new JPanel(new BorderLayout(0, 0));
		tabbedPane.addTab("Analyse", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/analyse.png")),
				analysePanel);
		statisticsPane = new StatisticsPane();
		analysePanel.add(statisticsPane, BorderLayout.LINE_END);
		
		editPanel.add(graph.getComponent(), BorderLayout.CENTER);
		tabbedPane.addChangeListener(this);
		tabbedPane.setSelectedComponent(editPanel);

		JToolBar viewToolBar = new JToolBar();
		viewToolBar.setFloatable(false);
		viewPanel.add(viewToolBar, BorderLayout.NORTH);

		JButton btnZoomIn = new JButton("Zoom in", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/zoomin.png")));
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mxGraphComponent component = graph.getComponent();
				component.zoomIn();
//				centerGraph();
			}
		});
		viewToolBar.add(btnZoomIn);

		JButton btnZoomOut = new JButton("Zoom out", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/zoomout.png")));
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mxGraphComponent component = graph.getComponent();
				component.zoomOut();
//				centerGraph();
			}
		});
		viewToolBar.add(btnZoomOut);

		JToolBar editToolBar = new JToolBar();
		editToolBar.setFloatable(false);
		editPanel.add(editToolBar, BorderLayout.NORTH);

		JButton btnAddVertex = new JButton("Add person", new ImageIcon(
				ClassLoader.getSystemClassLoader().getResource(
						"icons/addperson.png")));
		btnAddVertex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VertexValue value = AddVertexDialog.showDialog(frame);

				if (value != null) {
					graph.insertVertex(value);
					if(isGraphicalModeActive)
						graph.executeLayout();
				}
			}
		});
		editToolBar.add(btnAddVertex);
		btnAddVertex.setMnemonic(KeyEvent.VK_P);
		btnAddVertex.setToolTipText("Alt + P");

		JButton btnAddEdge = new JButton("Add relationship", new ImageIcon(
				ClassLoader.getSystemClassLoader().getResource(
						"icons/addrelationship.png")));
		btnAddEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] chosenVertices = ChoseVerticesDialog.showDialog(graph,
						frame, ChoseVerticesDialog.RELATIONSHIP);

				if(chosenVertices != null){
					graph.insertEdge((mxCell) chosenVertices[0],
									(mxCell) chosenVertices[1]);
					if(isGraphicalModeActive)
						graph.executeLayout();
				}
			}
		});
		editToolBar.add(btnAddEdge);
		btnAddEdge.setMnemonic(KeyEvent.VK_R);
		btnAddEdge.setToolTipText("Alt + R");

		btnOpenGraph = new JButton("Open graph", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/open.png")));
		btnOpenGraph.addActionListener(this);
		editToolBar.add(btnOpenGraph);

		btnSaveGraph = new JButton("Save graph", new ImageIcon(ClassLoader
				.getSystemClassLoader().getResource("icons/save.png")));
		btnSaveGraph.addActionListener(this);
		editToolBar.add(btnSaveGraph);

		JButton btnGenerateGraph = new JButton("Generate graph");
		btnGenerateGraph.setIcon(new ImageIcon(NewInfectionSim.class.getResource("/icons/cogs.png")));
		btnGenerateGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graph.reset();
				statisticsPane.reset();
				String path = GraphGenerator.generate(new File(
						"src/data/generated.xml"), 100, 3);
				GraphXMLParser parser = new GraphXMLParser(graph, null);
				parser.parse(path);
				graph.executeLayout();
				
				// Fit the window:
				mxGraphComponent component = graph.getComponent();
				mxRectangle rectangle = graph.getGraphBounds();
				double scale = frame.getHeight() / rectangle.getHeight();
				component.zoomTo(scale, true);
//				component.zoomAndCenter();
			}
		});
		editToolBar.add(btnGenerateGraph);

		JToolBar analyseToolBar = new JToolBar();
		analyseToolBar.setFloatable(false);
		analysePanel.add(analyseToolBar, BorderLayout.NORTH);

		JButton btnFindPath = new JButton("Simulate infection", new ImageIcon(
				ClassLoader.getSystemClassLoader().getResource(
						"icons/infect.png")));
		btnFindPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (graph.startAnimation() != 0) {
					graph.clearSelection();
					Object[] chosenVertices = ChoseVerticesDialog.showDialog(
							graph, frame, ChoseVerticesDialog.FIND_PATH);

					if (chosenVertices != null) {
						int length = graph.findShortestPath(chosenVertices[0],
												chosenVertices[1],
												graph.getSize(),
												statisticsPane,
												(Integer) chosenVertices[4]);
						if (length == 0)
							JOptionPane.showMessageDialog(frame,
									chosenVertices[2] + " cannot infect "
											+ chosenVertices[3], "Warning",
									JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		analyseToolBar.add(btnFindPath);

		JButton btnStopSimulation = new JButton("Stop simulation",
				new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
						"icons/infectstop.png")));
		btnStopSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph.stopAnimation();
			}
		});
		analyseToolBar.add(btnStopSimulation);

		JButton btnClearSimulation = new JButton("Clear results",
				new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
						"icons/infectclear.png")));
		btnClearSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph.stopAnimation();
				graph.clearSelection();
				statisticsPane.reset();
			}
		});
		analyseToolBar.add(btnClearSimulation);

		/*
		 * JButton btnClearSimulation = new JButton("Clear results", new
		 * ImageIcon
		 * (ClassLoader.getSystemClassLoader().getResource("icons/infectclear.png"
		 * ))); btnClearSimulation.addActionListener(new ActionListener() {
		 * public void actionPerformed(ActionEvent e) { graph.clearSelection();
		 * } }); analyseToolBar.add(btnClearSimulation);
		 */

		setLocationRelativeTo(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(mntmOpenGraph)
				|| e.getSource().equals(btnOpenGraph)) {
			graph.reset();
			statisticsPane.reset();
			JFileChooser chooser = new JFileChooser();
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					String path = chooser.getSelectedFile().getCanonicalPath();
					GraphXMLParser parser = new GraphXMLParser(graph, null);
					parser.parse(path);
					
					if(graph.getVertices().size() > 200)
						isGraphicalModeActive = false;
					else
						isGraphicalModeActive = true;

					if(isGraphicalModeActive) {
//						centerGraph();
						graph.executeLayout();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource().equals(mntmSaveGraph)
				|| e.getSource().equals(btnSaveGraph)) {
			JFileChooser chooser = new JFileChooser();
			int result = chooser.showSaveDialog(this);

			if (result == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				try {
					GraphToXMLExporter.export(graph, file);
					JOptionPane.showMessageDialog(this, "Succesfully saved as "
							+ file.getName(), "Succes",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this,
							"An error occurred, file cannot be saved\n"
									+ "Exception: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
		else if (e.getSource().equals(mntmGenerateGraph)) {
			String path = GeneratorDialog.show(this);
			if(path != null){
				JOptionPane.showMessageDialog(this,
											"Graph successfully generated\n" + path,
											"Success",
											JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (Arrays.asList(mntmToPng, mntmToJpg, mntmToGif, mntmToBmp)
				.contains(e.getSource())) {
			JFileChooser chooser = new JFileChooser();
			int result = chooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					String format = "png"; // defaultowo jest do PNG
					if (e.getSource().equals(mntmToJpg))
						format = "jpg";
					else if (e.getSource().equals(mntmToGif))
						format = "gif";
					else if (e.getSource().equals(mntmToBmp))
						format = "bmp";

					File file = chooser.getSelectedFile();
					GraphToImageExporter.export(file, format, graph);
					JOptionPane.showMessageDialog(this,
							"Succesfully exported to " + file.getName(),
							"Succes", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this,
							"An error occurred, file cannot be saved\n"
									+ "Exception: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(tabbedPane)) { // graph.getComponent() jest
												// dodawany do aktualnie
												// wybranego panelu
			JPanel selectedPane = (JPanel) tabbedPane.getSelectedComponent();
			selectedPane.add(graph.getComponent(), BorderLayout.CENTER);
		}
	}
	
	public void centerGraph() {
		// center graph:
		mxRectangle bounds = graph.getComponent().getGraph().getGraphBounds();
		int positionX = (int) ((editPanel.getWidth() - bounds.getWidth())/2.0);
		int positionY = (int) ((editPanel.getHeight() - bounds.getHeight())/2.0);
		graph.getComponent().getGraph().getView().setTranslate(new mxPoint(positionX, positionY));
	}

}
