package objets;

import java.awt.Color;

// Un mur est un point avec des getters.
// La classe fournit deux tableaux de murs (les verticaus et les horizontaux), et la longueur de chaque type de mur.

public class Mur {

	public static final int longueurH = 200;
	public static final int longueurV = 1250;
	public static final Mur MursH[] = {new Mur(longueurH,400), new Mur(2584+longueurH,400)};
	public static final Mur MursV[] = {new Mur(444,1850 + longueurV),
									   new Mur(1138,1850 + longueurV),
									   new Mur(1832,1850 + longueurV),
									   new Mur(2526,1850 + longueurV)};
	
	private int x, y;
	Color couleurMur = Color.BLACK;
	
	public Mur(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
