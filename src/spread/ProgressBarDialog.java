package spread;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class ProgressBarDialog extends JFrame
		implements GraphXMLParserListener, ActionListener {
	
	private JLabel message = new JLabel("", JLabel.LEFT);
	private JProgressBar progressBar = new JProgressBar();
	private int taskSize = 0;
	private JButton button = new JButton("OK");
	
	public ProgressBarDialog(Component parent) {
		if(parent != null)
			this.setLocation(parent.getLocationOnScreen());
		setLayout(new GridLayout(3, 1));
		setPreferredSize(new Dimension(500, 120));
		
		JPanel buttonPane = new JPanel(new BorderLayout());
		buttonPane.add(button, BorderLayout.PAGE_END);
		button.setEnabled(false);
		button.addActionListener(this);
		
		add(message);
		add(progressBar);
		add(buttonPane);
		
		pack();
		setVisible(true);
	}

	@Override
	public void parserStateChanged(ParserStateChange change) {
		message.setText(change.getInfo());
		progressBar.setValue(change.getProgress());
	}

	@Override
	public void taskStarted(ParserStateChange change) {
		message.setText(change.getInfo());
		progressBar.setMinimum(0);
		taskSize = change.getTaskSize();
		progressBar.setMaximum(taskSize);
		this.setVisible(true);
	}

	@Override
	public void taskFinished(Object source) {
		message.setText("Finished");
		progressBar.setValue(taskSize);
		button.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
	
	public static void main(String[] args) {
		(new ProgressBarDialog(null)).setVisible(true);
	}
}
