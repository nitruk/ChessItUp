package objets;


import java.awt.Color;
import java.awt.Graphics;

public class Case {

	private int cote = 1;
	private int x, y;
	Color couleurCase = Color.BLACK;
	boolean couleur = true;
	boolean bonus = false;
	
	public Case(int x, int y, int cote, boolean couleur) {
		this.x = (int) x/3;
		this.y = (int) y/3;
		this.couleur = couleur;
		couleurCase = (couleur) ? Color.BLUE : Color.RED;
		this.cote = (int) Math.floor(cote/3);
	}
	
	public int getX() {
		return 3*x;
	}
	public int getY() {
		return 3*y;
	}
	public boolean getColor() {
		return couleur;//True = Bleu ; False = Rouge
	}
	public boolean getBonus() {
		return couleur;
	}
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	
	public void dessiner (Graphics g) {
		g.setColor(couleurCase);
		g.fillRect(x-cote/2, y-cote/2, cote, cote);
		if (bonus) {
			g.setColor(Color.BLACK);
			g.fillOval(x-cote/6, y-cote/6, cote/3, cote/3);
		}
	}
}
