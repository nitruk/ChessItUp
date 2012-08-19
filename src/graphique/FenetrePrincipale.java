package graphique;

import javax.swing.JFrame;

// La fenêtre se contente de créer son ContentPane et d'apparaître

public class FenetrePrincipale{

	public static void main(String[] args) {
		System.out.println("Bonjour !");
		JFrame fenetre = new JFrame("Eurobot 2011 - client");
		fenetre.setContentPane(new PanneauGeneral());
		fenetre.pack();
		fenetre.setVisible(true);
		fenetre.setResizable(false);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}