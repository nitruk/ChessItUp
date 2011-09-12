package objets;


import java.awt.Color;
import java.awt.Graphics;

public class Mur {

	private int x1, y1, x2, y2;
	Color couleurMur = Color.BLACK;
	
	public Mur(int x1, int y1, int x2, int y2) {
		this.x1 = x1/3;
		this.y1 = y1/3;
		this.x2 = x2/3;
		this.y2 = y2/3;
	}
	
	public int getX1() {
		return 3*x1;
	}
	public int getY1() {
		return 3*y1;
	}
	public int getX2() {
		return 3*x2;
	}
	public int getY2() {
		return 3*y2;
	}
	
	public void dessiner (Graphics g) {
		g.setColor(couleurMur);
		g.drawLine(x1, y1, x2, y2);
	}
	
}
