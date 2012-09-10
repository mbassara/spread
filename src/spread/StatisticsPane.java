package spread;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import SpringUtilities.SpringUtilities;

@SuppressWarnings("serial")
public class StatisticsPane extends JPanel {
	
	private JLabel healthyLabel;
	private JLabel healthyNumberLabel;
	private JLabel inProgressLabel;
	private JLabel inProgressNumberLabel;
	private JLabel infectedLabel;
	private JLabel infectedNumberLabel;
	
	private JScrollPane messagePane;
	private JTextArea messageTextArea;
	private String message = "";
	private int messageNumber = 1;
	
	public StatisticsPane() {
		setLayout(new BorderLayout());
		JPanel innerPane = new JPanel(new SpringLayout());
		JPanel tmpPane = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Statistics", JLabel.CENTER);
		
		healthyLabel = new JLabel("Healthy:", JLabel.RIGHT);
		healthyNumberLabel = new JLabel("0", JLabel.RIGHT);
		inProgressLabel = new JLabel("Infection in progress:", JLabel.RIGHT);
		inProgressNumberLabel = new JLabel("0", JLabel.RIGHT);
		infectedLabel = new JLabel("Infected:", JLabel.RIGHT);
		infectedNumberLabel = new JLabel("0", JLabel.RIGHT);
		
		messageTextArea = new JTextArea();
		messageTextArea.setEditable(false);
		messageTextArea.setMargin(new Insets(0, 10, 10, 10));
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messagePane = new JScrollPane(messageTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		Font font = title.getFont();
		title.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * 1.5)));
		title.setPreferredSize(new Dimension(300, 45));

		healthyLabel.setPreferredSize(new Dimension(250, 25));
		healthyNumberLabel.setPreferredSize(new Dimension(50, 25));
		inProgressLabel.setPreferredSize(new Dimension(250, 25));
		inProgressNumberLabel.setPreferredSize(new Dimension(50, 25));
		infectedLabel.setPreferredSize(new Dimension(250, 25));
		infectedNumberLabel.setPreferredSize(new Dimension(50, 25));
		
		innerPane.add(healthyLabel);
		innerPane.add(healthyNumberLabel);
		innerPane.add(inProgressLabel);
		innerPane.add(inProgressNumberLabel);
		innerPane.add(infectedLabel);
		innerPane.add(infectedNumberLabel);
		
		SpringUtilities.makeCompactGrid(innerPane, 3, 2, 5, 5, 5, 5);
		
		add(title, BorderLayout.PAGE_START);
		tmpPane.add(innerPane, BorderLayout.PAGE_START);
		tmpPane.add(messagePane, BorderLayout.CENTER);
		add(tmpPane, BorderLayout.CENTER);

	}
	
	public void setHealthy(int num) {
		healthyNumberLabel.setText(String.valueOf(num));
	}
	
	public void setInProgress(int num) {
		inProgressNumberLabel.setText(String.valueOf(num));
	}
	
	public void setInfected(int num) {
		infectedNumberLabel.setText(String.valueOf(num));
	}
	
	public void setMessage(String message) {
		this.message = message;
		messageNumber = 1;
		messageTextArea.setText(messageNumber + ". " + message);
	}
	
	public void appendMessage(String newLines) {
		if(newLines.length() < 3)
			message = message + newLines + "\n";
		else
			message = message + (messageNumber++) + ". " + newLines + "\n";
		messageTextArea.setText(message);
	}
	
	public void reset() {
		healthyNumberLabel.setText("0");
		inProgressNumberLabel.setText("0");
		infectedNumberLabel.setText("0");
		messageTextArea.setText("");
		message = "";
		messageNumber = 1;
	}
	
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame();
		StatisticsPane stats = new StatisticsPane();
//		stats.setHealthy(0);
		frame.add(stats);
		frame.pack();
		frame.setVisible(true);
		stats.setMessage("raz dwa\ntrzy cztery\n\tpiec");
		
//		Thread.sleep(2000);
//		stats.setHealthy(200);
	}

}
