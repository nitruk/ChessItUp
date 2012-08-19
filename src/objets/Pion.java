package objets;


import java.awt.Color;
import java.io.Serializable;

public class Pion implements Serializable {

	public static final int PAWN = 1;
	public static final int QUEEN = 2;
	public static final int KING = 3;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y;
	public static int diametre = 200;
	private Color couleur;
	private int figure = PAWN; //0 = pion ; 1 = reine ; 2 = roi;
	private int nombre = 1;
	static int nbPions = 0;
	private int index;
	private boolean inRobot = false, utile = true;
	
// Un point avec des getters et un setter indirect, des dimensions et une couleur
	
	public Pion(int x, int y, int figure) {
		this.x = x;
		this.y = y;
		this.figure = figure;
		couleur = (figure!=Pion.PAWN) ? Color.ORANGE : Color.YELLOW;
		nbPions ++;
		index = nbPions-1;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getCouleur() {
		return couleur;
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public float getIndex() {
		return index;
	}
	
	public float getRayon () {
		return diametre/2;
	}
	
// Le pion est-il dans un robot?

	public boolean getInRobot (){
		return inRobot;
	}
	
	public void setInRobot (boolean inRobot){
		this.inRobot = inRobot;
	}

// Le pion représente-t-il une pile ? Pour combien d'éléments compte-t-il ? Y a-t-il une figure au sommet ?
	
	public boolean getinPile (){
		return nombre > 1;
	}
	
	public int getNombre (){
		return nombre;
	}
	
	public void setInPile (Pion pile){
		utile = false;
		pile.incrPile();
	}
	
	public void incrPile (){
		nombre ++;
	}
	
	public int getFigure() {
		return figure; //0 = pion ; 1 = reine ; 2 = roi;
	}

// Le pion correspond-il encore à quelque chose ? Ou bien a-t-il été incorporé à une pile représentée par un autre pion ?

	public boolean getUtile (){
		return utile;
	}
}
