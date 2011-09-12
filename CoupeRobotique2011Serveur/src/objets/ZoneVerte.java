package objets;


import java.awt.Color;
import java.awt.Graphics;

public class ZoneVerte {

	private int grandCote = (2100-400)/3;
	private int petitCote = 400/3;
	private int xGauche, yHaut;
	Color couleur = Color.GREEN;
	
	public ZoneVerte(int xGauche, int yHaut) {
		this.xGauche = (int) xGauche/3;
		this.yHaut = (int) yHaut/3;
	}
	
	public void dessiner (Graphics g) {
		g.setColor(couleur);
		g.fillRect(xGauche, yHaut, petitCote, grandCote);
	}
}
