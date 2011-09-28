package graphique;


import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import objets.Robot;

public class PanneauInfo extends JPanel {
	static final long serialVersionUID = 1;
	Color couleurFond = Color.GRAY;
	private JLabel status;
	
	public PanneauInfo(Robot robots[]) {
		//setPreferredSize(new Dimension (200,700));
		setBackground(couleurFond);
		
		JLabel label;
		for (int n = 0 ; n < 2 ; n ++) {
			label = new JLabel(robots[n].getName());
			label.setForeground(robots[n].getCouleur());
			add(label);
		}
		status = new JLabel("waiting");
		add(status);
	}
	
	public void setStatus (String message) {
		status.setText(message);
	}
}