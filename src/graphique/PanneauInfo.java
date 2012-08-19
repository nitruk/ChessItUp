package graphique;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import objets.Robot;

public class PanneauInfo extends JPanel {
	static final long serialVersionUID = 1;
	Color couleurFond = Color.GRAY;
	private JLabel status;
	private JLabel noms[] = new JLabel[2];
	
// Affichage de l'état du robot contrôlé et des 2 noms des joueurs avec leurs couleurs
	
	public PanneauInfo(Robot robots[]) {
		setPreferredSize(new Dimension (200,696));
		setLayout(new GridLayout(12,1));
		setBackground(couleurFond);
		for (int n = 0 ; n < 2 ; n ++) {
			noms[n] = new JLabel(robots[n].getName());
			noms[n].setForeground(robots[n].getCouleur());
			add(noms[n]);
		}
		status = new JLabel("waiting");
		add(status);
	}
	
	public void setStatus (String message) {
		status.setText(message);
	}
	
// Affichage des scores
	
	public void end (int score0, int score1) {
		noms[0].setText(noms[0].getText() + " : " + score0);
		noms[1].setText(noms[1].getText() + " : " + score1);
	}
}