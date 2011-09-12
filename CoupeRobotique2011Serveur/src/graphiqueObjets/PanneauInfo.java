package graphiqueObjets;


import graphique.PanneauGeneral;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanneauInfo extends JPanel implements ActionListener{
	static final long serialVersionUID = 1;
	Color couleurFond = Color.GRAY;
	PanneauGeneral panneauGeneral;
	
	public PanneauInfo(PanneauGeneral panneauGeneral) {
		this.panneauGeneral= panneauGeneral;
		setPreferredSize(new Dimension (200,700));
		setBackground(couleurFond);
		
		JButton demarrer = new JButton("Demarrer");
		demarrer.addActionListener(this);
		demarrer.setActionCommand("bouton_demarrer");
		
		Box contenu = Box.createVerticalBox();
		add(contenu);
		contenu.add(demarrer);
	}
	public void actionPerformed (ActionEvent ae) {
		String nom = ae.getActionCommand();
		if (nom=="bouton_demarrer") { //Début du match
			panneauGeneral.getTableJeu().getFonctions().coupSuivant(true);
			panneauGeneral.getTableJeu().getFonctions().coupSuivant(false);
		}
	}
}