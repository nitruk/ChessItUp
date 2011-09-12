package graphique;

import javax.swing.JFrame;

import serveur.Serveur;

public class FenetrePrincipale{

	public static void main(String[] args) {
		JFrame fenetre = new JFrame("Eurobot 2011 - serveur");
		fenetre.setContentPane(new PanneauGeneral());       //Affiche le panneau général
		fenetre.pack();
		fenetre.setVisible(true);
		fenetre.setResizable(false);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new Serveur();
	}
}