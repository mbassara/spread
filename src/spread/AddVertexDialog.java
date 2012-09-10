package spread;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import SpringUtilities.SpringUtilities;

public class AddVertexDialog {
	public static VertexValue showDialog(Component parent) {
		String[] label = {"Id: ", "Name: ", "Surname: ", "Immunity: "};
		JTextField[] text = new JTextField[label.length];
		
		JPanel panel = new JPanel(new SpringLayout());
		for(int i = 0; i < label.length; i++){
			JLabel lab = new JLabel(label[i], JLabel.TRAILING);
			panel.add(lab);
			text[i] = new JTextField();
			lab.setLabelFor(text[i]);
			panel.add(text[i]);
		}
		
		SpringUtilities.makeCompactGrid(panel,
										4, 2,
										5, 5,
										5, 5);
		
		int result = JOptionPane.showConfirmDialog(parent, panel, 
					"Add Person", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/addpersonbig.png")));
		
		if (result == JOptionPane.OK_OPTION) {
			boolean allFieldsFilledIn = true;
			for(int i = 0; i < label.length; i++)
				if(text[i].getText().length() == 0)
					allFieldsFilledIn = false;
			if(allFieldsFilledIn){
				return new VertexValue(text[0].getText(),
										text[1].getText(),
										text[2].getText(),
										Double.parseDouble(text[3].getText()));
			} else {
				JOptionPane.showMessageDialog(parent, "You have to fill in every field!",
													"Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		else {
			return null;
		}
	}
}